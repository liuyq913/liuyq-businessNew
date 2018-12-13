package com.liuyq.log2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *     类似linux的tail命令
 * </p>
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/4.21:02
 * @see
 */
@Component
public class LogTailer extends LogReader {

    private static final Logger log = LoggerFactory.getLogger(LogTailer.class);

    public ByteWrapper tail(int line, String path, Integer sessionId) throws IOException {
        try {
            if (sessionId != null) return tailWithSession(line, path, sessionId);
            return tailWithThread(line, path);
        } finally {
            recycle();  // 释放
        }
    }

    /**
     * 基于{@link Session}的日志查看，可以保持状态一致
     * @param line
     * @param path
     * @param sessionId 会话ID，具有唯一性，查看不同的日志文件，需要不同的ID
     * @return
     * @throws IOException
     */
    private ByteWrapper tailWithSession(int line, String path, Integer sessionId) throws IOException {
        SessionCache sessionCache = null;
        try {
            Session session = Session.get(sessionId);
            if (session == null)
                throw new IllegalStateException("The Session was been used or not exist ! check your sessionId");
            sessionCache = SessionCache.get(session, this, getLogPath(path));

            int readBufferSize = logConfig.getReadBufferSize();
            tailByModify(line, sessionCache, readBufferSize);
            tail(line, sessionCache, readBufferSize);

            int lastLine = sessionCache.getLastLine();
            return sessionCache.getLineByteWrapper().calculateOffsetAndLenTail(line > lastLine ? lastLine : line, 0);
        } finally {
            if (sessionCache != null) sessionCache.recycle();
        }
    }

    private ByteWrapper tailWithThread(int line, String path) throws IOException {
        ThreadLocalCache threadLocalCache = null;
        try {
            threadLocalCache = getThreadLocalCache(line, path);    // 获取缓存

            int readBufferSize = logConfig.getReadBufferSize();

            int result = tailByModify(line, threadLocalCache, readBufferSize);   // 日志有修改（追加），需要先处理
            if (COMPLETE != result) tail(line, threadLocalCache, readBufferSize);

            int lastLine = threadLocalCache.getLastLine();
            ByteWrapper byteWrapper = threadLocalCache.getLineByteWrapper();
            byteWrapper.calculateOffsetAndLenTail(line > lastLine ? lastLine : line, 0);
            return byteWrapper;
        } finally {
            if (threadLocalCache != null) threadLocalCache.recycle();
        }
    }

    /**
     * 如果日志有修改（追加），读取追加的部分
     * @param line
     * @param logCache
     * @param readBufferSize
     * @throws IOException
     * @return
     */
    private int tailByModify(int line, LogCache logCache, int readBufferSize) throws IOException {
        LogFile logFile = logCache.getLogFile();
        long lastFileSize = logCache.getLastFileSize();
        long fileSize = logFile.getFileSize();
        //log.info("Log Reader fileSize is {} and lastModifyTime is {}", fileSize, lastFileSize);
        //log.info("Log Reader Cache lastFileSize is {} and lastModifyTime is {}", lastFileSize, logCache.getLastModifyTime());

        boolean isRebuild;
        if ((! (isRebuild = logFile.isRebuild())) && fileSize <= lastFileSize) return SUCCESS;
        if (isRebuild) {
            logCache.setLastModifyTime(logFile.getLastModifyTimeMark());
            logCache.setLastFileSize(0);
            logCache.reuse();
            lastFileSize = 0;
        }
        logCache.setLastFileSize(fileSize);

        TailParser parser = new TailParser(logCache, true);
        long modifyLastPos = fileSize, initPos = lastFileSize;
        int result;
        boolean isRemove = false;   // 是否清除了之前读的日志缓存，目前，当空间不够时，会清除所有上次缓存的日志
        for (;;) {  // 循环读
            long pos = position(modifyLastPos, initPos, readBufferSize);
            parseLineTail(logFile.read(pos), modifyLastPos, pos, parser);
            modifyLastPos -= parser.posCounters;    // 在complete之前
            result = parser.complete();
            if (REMOVE == result) isRemove = true;

            // 校验
            if (COMPLETE == result) {
                logCache.setLastPos(modifyLastPos + parser.posCounter);
                logCache.setLastLine(logCache.getLastLine() + parser.lines - parser.lineCounter);
                break;
            }
            if (line <= parser.lines) { // 行
                // 清除之前缓存的行
                logCache.remove(logCache.getLastLine());
                logCache.setLastPos(modifyLastPos);
                logCache.setLastLine(parser.lines);
                break;
            }
            if (modifyLastPos == initPos || modifyLastPos - 1 == initPos) {
                logCache.setLastLine(logCache.getLastLine() + parser.lines);
                if (isRemove) logCache.setLastPos(initPos);
                break;
            }
        }
        return result;
    }

    private void tail(int line, LogCache logCache, int readBufferSize) throws IOException {
        int lastLine = logCache.getLastLine();
        if (lastLine >= line) return;

        LogFile logFile = logCache.getLogFile();
        TailParser parser = new TailParser(logCache, false);
        long lastPos = logCache.getLastPos(), lastPosMark = 0;
        for (;;) {
            long pos = position(lastPos, 0, readBufferSize);
            parseLineTail(logFile.read(pos), lastPos, pos, parser);
            int posCounters = parser.posCounters;
            int result = parser.complete();
            lastPos = logCache.getLastPos();

            // 校验
            if (COMPLETE == result) {
                logCache.setLastPos(logCache.getLastPos() + posCounters);
                logCache.setLastLine(logCache.getLastLine() + parser.lines - parser.lineCounter);
                break;
            }
            if (lastLine + parser.lines >= line || (lastPos ^ lastPosMark) == 0 || parser.lines == 0) {    // 超过容量的无法获取，先这样吧
                logCache.setLastLine(logCache.getLastLine() + parser.lines);
                break;
            }
            lastPosMark = lastPos;
        }
    }

    /**
     * 解析器，高度定制
     */
    class TailParser extends Parser {

        private final LogCache logCache;
        private final boolean isModify; // 本次解析的数据是否属于来自tailByModify

        private int lines;  // 解析的行数
        private int posCounters;   // 所有posCounter的总和
        private List<Integer> lens; // 每次parse的行的长度信息的列表
        private int tailLen;    // 用于tailByModify，表示一次buffer读取解析的行数
        private Integer bufferOffset;   // 之前读取的数据的总偏移量

        TailParser(LogCache logCache, boolean isModify) {
            this.logCache = logCache;
            this.isModify = isModify;
            this.lens = new ArrayList<>(50);    // 4k一般在40~50行之间
        }

        void reuse() {
            //lines = 0;
        }

        @Override
        protected void parse() {
            if (isModify) parseModify();
            else {
                lens.add(len);
                // 其它
                logCache.addLastPos(-posCounter);
                posCounters += posCounter;
                lines ++;
                tailLen += len;
            }
        }

        private void parseModify() {
            lens.add(len);
            // 其它
            posCounters += posCounter;
            lines ++;
            tailLen += len;
        }

        @Override
        protected void parseTail() {
            if (isModify && lineCounter == 0) {   // 如果是在上次的基础上读取且只有一行
                lineCounter ++;
                parseModify();
            }
        }

        int complete() {
            PooledByteBuffer lineBuffer = logCache.getLineBuffer();
            ByteWrapper byteWrapper = logCache.getLineByteWrapper();
            PooledByteBuffer lineTmpBuffer = logCache.getLineTmpBuffer();
            int result;
            if (isModify) {
                bufferOffset(byteWrapper);
                // 校验
                lineBuffer.flip();
                result = checkByModify(lineBuffer, lineTmpBuffer);
                if (COMPLETE == result) return result;
                else if (REMOVE == result) {
                    lineBuffer = logCache.getLineBuffer();
                    lineTmpBuffer = logCache.getLineTmpBuffer();
                    lineBuffer.flip();
                }

                Collections.reverse(lens);
                byteWrapper.addOffsetMapAll(logCache.getLastLine() + 1, lens);

                // copy
                lineTmpBuffer.copy(lineBuffer, 0, bufferOffset);
                lineTmpBuffer.copy(pooledBuffer, size - posCounters, posCounters);
                lineTmpBuffer.copy(lineBuffer, bufferOffset, lineBuffer.buffer().remaining() - bufferOffset);
            } else {
                // 校验
                bufferOffset = byteWrapper.getOffsetLast();
                if (! checkCapacity(bufferOffset, posCounters, lineTmpBuffer)) return COMPLETE;
                else result = SUCCESS;

                lineBuffer.flip();
                Collections.reverse(lens);
                byteWrapper.addOffsetMapFirstAll(lens);

                // copy
                lineTmpBuffer.copy(pooledBuffer, size - posCounters, posCounters);
                lineTmpBuffer.copy(lineBuffer, 0, bufferOffset);
            }
            complete0();
            return result;
        }

        /**
         * 校验tailByModify的读数据是否可用。
         * @param lineBuffer 保存了数据的缓冲区
         * @param lineTmpBuffer
         * @return
         */
        private int checkByModify(PooledByteBuffer lineBuffer, PooledByteBuffer lineTmpBuffer) {
            while (! checkCapacity(lineBuffer.buffer().remaining(), posCounters, lineTmpBuffer)) {
                if (bufferOffset > 0 && posCounters > 0) {
                    logCache.remove(logCache.getLastLine());  // 清除之前缓存的行数据
                    logCache.setLastLine(0);
                    this.bufferOffset = 0;
                    log.info("Log Tailer read modify and remove cache log !");
                    return REMOVE;
                } else return COMPLETE;
            }
            return SUCCESS;
        }

        private void complete0() {
            logCache.resetLineBuffer();
            lens.clear();
            tailLen = 0;
            posCounters = 0;
        }

        private int bufferOffset(ByteWrapper byteWrapper) {
            if (bufferOffset == null) bufferOffset = byteWrapper.getOffsetLast();
            return bufferOffset;
        }
    }
}
