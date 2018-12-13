package com.liuyq.log2;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     类似linux的head命令
 * </p>
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/5.10:44
 * @see
 */
@Component
public class LogHeader extends LogReader {

    @Override
    protected void doInitLogCache(LogCache logCache) {
        logCache.setLastPos(0);
    }

    public ByteWrapper head(int line, String path) throws IOException {
        ThreadLocalCache threadLocalCache = null;
        try {
            threadLocalCache = getThreadLocalCache(line, path);

            checkModifyAndInit(threadLocalCache);
            head(line, threadLocalCache);

            int lastLine = threadLocalCache.getLastLine();
            return threadLocalCache.getLineByteWrapper().calculateOffsetAndLen(line > lastLine ? lastLine : line, 0);
        } finally {
            if (threadLocalCache != null) threadLocalCache.recycle();
            recycle();
        }
    }

    private void head(int line, LogCache logCache) throws IOException {
        int lastLine = logCache.getLastLine();
        if (lastLine >= line) return;

        LogFile logFile = logCache.getLogFile();
        HeadParser parser = new HeadParser(logCache);
        long lastPos = logCache.getLastPos();
        for (;;) {
            long pos = lastPos;
            parseLineHead(logFile.read(pos), lastPos, pos, parser);
            if (COMPLETE == parser.complete()) break;
            // 修改缓存参数
            logCache.setLastPos(logCache.getLastPos() + parser.posCounters);
            int lines = parser.lines;
            logCache.setLastLine(logCache.getLastLine() + lines);
            parser.complete0();

            // 校验
            if (logCache.getLastLine() >= line || lines == 0) {
                break;
            }

            lastPos = logCache.getLastPos();
        }
    }

    /**
     * 判断日志文件是否修改，不是追加
     * @param logCache
     * @throws IOException
     */
    private void checkModifyAndInit(LogCache logCache) throws IOException {
        LogFile logFile = logCache.getLogFile();
        if (! logFile.isRebuild()) return;

        logCache.setLastModifyTime(logFile.getLastModifyTimeMark());
        logCache.reuse();
    }

    class HeadParser extends Parser {

        private final LogCache logCache;

        private int posCounters;
        private int bufferOffset;   // 之前读取的数据的数量
        private List<Integer> lens; // 每次parse的行的长度信息的列表
        private int lines;

        HeadParser(LogCache logCache) {
            this.logCache = logCache;
            this.lens = new ArrayList<>(50);    // 4k一般在40~50行之间
        }

        @Override
        protected void parse() {
            posCounters += posCounter;
            lens.add(len);
            lines ++;
        }

        @Override
        protected void parseTail() {
            parse();
        }

        int complete() {
            PooledByteBuffer lineBuffer = logCache.getLineBuffer();
            ByteWrapper byteWrapper = logCache.getLineByteWrapper();

            bufferOffset = byteWrapper.getOffsetLast();
            if (! checkCapacity(bufferOffset, posCounters, lineBuffer)) return COMPLETE;

            byteWrapper.addOffsetMapAll(lens);

            // copy，只添加
            lineBuffer.copy(pooledBuffer, 0, posCounters);
            return SUCCESS;
        }

        void complete0() {
            lines = 0;
            posCounters = 0;
            lens.clear();
        }
    }
}
