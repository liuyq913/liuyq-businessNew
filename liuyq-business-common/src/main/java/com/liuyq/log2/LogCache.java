package com.liuyq.log2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     一个缓存有且只有一个{@link #readBuffer}、{@link #lineBuffer}、{@link #lineByteWrapper}
 * </p>
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/4.20:41
 * @see
 */
public abstract class LogCache {

    private static final Logger log = LoggerFactory.getLogger(LogCache.class);

    private PooledBufferAllocator allocator = PooledBufferAllocator.DEFAULT;

    private PooledByteBuffer readBuffer;    // IO读数据的缓冲区
    private PooledByteBuffer lineBuffer;    // 用于缓存读取的行日志数据
    private ByteWrapper lineByteWrapper;    // lineBuffer的包装类，主要用于对外使用
    private PooledByteBuffer lineTmpBuffer; // lineBuffer的copy对象，数据发生移动时，两者不断替换，需要及时释放，达到不占用太多内存的目的

    protected LogFile logFile;
    protected LogConfig logConfig;
    protected LogReader logReader;
    protected String logPath; // 对应日志文件的路径

    // 缓存信息
    private long lastPos;   // 日志上次读的位置
    private long lastFileSize;  // 上次获取的文件大小，用于判断日志文件是否有追加
    private long lastModifyTime;    // 日志文件的上次修改时间
    private int lastLine;   // 上次读留下来的行数

    protected long lastUseTime;   // 缓存的上次时候时间，用于缓存回收的判断
    protected static volatile LogCacheTask logCacheTask;

    @Deprecated
    private LineList lineList;  // 行字符串列表

    public LogCache(LogReader logReader, LogConfig logConfig, LogFile logFile, String logPath) {
        this(logReader, logConfig, logFile, logPath, true);
    }

    public LogCache(LogReader logReader, LogConfig logConfig, LogFile logFile, String logPath, boolean addToTask) {
        this.logReader = logReader;
        this.logConfig = logConfig;
        this.logFile = logFile;
        this.logPath = logPath;
        initLogCacheTask(addToTask);
    }

    private void initLogCacheTask(boolean addToTask) {
        if (logCacheTask == null) {
            synchronized (LogCache.class) {
                if (logCacheTask == null) {
                    logCacheTask = new LogCacheTask(logConfig.getCacheRecycleScheduleTime());
                    logCacheTask.start();
                }
            }
        }
        if (addToTask) logCacheTask.addLogCache(this);
    }

    public PooledByteBuffer getReadBuffer() {
        if (readBuffer == null) {
            int readBufferSize = logConfig.getReadBufferSize();
            readBuffer = allocator.buffer(readBufferSize, readBufferSize << 1);
        }
        return readBuffer;
    }

    public LogFile getLogFile() {
        return logFile;
    }

    public String getLogPath() {
        return logPath;
    }

    public PooledByteBuffer getLineBuffer() {
        if (lineBuffer == null) {
            lineBuffer = allocator.buffer(logConfig.getInitLineBufferSize(), logConfig.getMaxLineBufferSize());
            lineByteWrapper = ByteWrapper.get(lineBuffer);
        }
        return lineBuffer;
    }

    public PooledByteBuffer getLineTmpBuffer() {
        if (lineTmpBuffer == null) {
            lineTmpBuffer = allocator.buffer(logConfig.getInitLineBufferSize(), logConfig.getMaxLineBufferSize());
        } else lineTmpBuffer.buffer().clear();
        return lineTmpBuffer;
    }

    public ByteWrapper getLineByteWrapper() {
        if (lineByteWrapper == null) getLineBuffer();
        return lineByteWrapper;
    }

    public long getLastPos() {
        return lastPos;
    }

    public void setLastPos(long lastPos) {
        this.lastPos = lastPos;
    }

    public void addLastPos(long pos) {
        this.lastPos += pos;
    }

    public long getLastFileSize() {
        return lastFileSize;
    }

    public void setLastFileSize(long lastFileSize) {
        this.lastFileSize = lastFileSize;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public int getLastLine() {
        return lastLine;
    }

    public void setLastLine(int lastLine) {
        this.lastLine = lastLine;
    }

    protected abstract long getCacheTime();

    public void reuse() {
        lastPos = 0;
        lastLine = 0;
        if (lineBuffer != null) {
            lineBuffer.recycle();
            lineBuffer = null;
        }
        if (lineByteWrapper != null) {
            lineByteWrapper.recycle();
            lineByteWrapper = null;
        }
    }

    /**
     * 缓存回收
     */
    public void recycle() {
        this.lastUseTime = System.currentTimeMillis();
    }

    /**
     * 周期回收
     */
    public void recycleSchedule() {
        if (lineBuffer != null) {
            lineBuffer.recycle();
            lineByteWrapper.recycle();
        }
        if (lineTmpBuffer != null) lineTmpBuffer.recycle();
        if (readBuffer != null) readBuffer.recycle();

        if (logFile != null) logFile.recycle();

        doRecycleSchedule();
    }

    protected abstract void doRecycleSchedule();

    // 添加/删除 //////////////////////////////////////////////////////////////////////////////////////////////////////

    public void add(PooledByteBuffer pooledBuffer, int offset, int len, int lineCounter) {
    }

    public void addLast(PooledByteBuffer pooledBuffer, int offset, int len, int lineCounter) {
        lineBuffer.buffer().put(pooledBuffer.buffer().array(), offset, len);    // 加入
        lineByteWrapper.addOffsetMap(len);
    }

    /**
     * 移除指定行的数据
     * @param line 从头开始的行数
     */
    public void remove(int line) {
        // 拷贝
        int offset = lineByteWrapper.getOffset(line);
        if (offset == 0) return;
        PooledByteBuffer lineBuffer = getLineBuffer().flip();
        if (lineBuffer.buffer().remaining() > offset)
            getLineTmpBuffer().copy(lineBuffer, offset, lineBuffer.buffer().remaining() - offset);

        // 重置
        resetLineBuffer();
        lineByteWrapper.removeOffsetMap(line);
    }

    public void resetLineBuffer() {
        this.lineBuffer.recycle();
        this.lineBuffer = this.lineTmpBuffer;
        this.lineTmpBuffer = null;
        this.lineByteWrapper.pooledBuffer = this.lineBuffer;
    }

    @Override
    public String toString() {
        return "LogCache{" +
                "allocator=" + allocator +
                ", readBuffer=" + readBuffer +
                ", lineBuffer=" + lineBuffer +
                ", lineByteWrapper=" + lineByteWrapper +
                ", lineTmpBuffer=" + lineTmpBuffer +
                ", logFile=" + logFile +
                ", logConfig=" + logConfig +
                ", logReader=" + logReader +
                ", logPath='" + logPath + '\'' +
                ", lastPos=" + lastPos +
                ", lastFileSize=" + lastFileSize +
                ", lastModifyTime=" + lastModifyTime +
                ", lastLine=" + lastLine +
                ", lastUseTime=" + lastUseTime +
                '}';
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 用于存储已经解码好的行字符串，用于临时过渡，因为这个方式需要进行字节和字符串的转换，消耗比较大
     */
    @Deprecated
    private class LineList {

        Line head;
        Line tail;
    }

    @Deprecated
    private class Line {

        private String line;    // 对行字符串的引用

        private Line prev;
        private Line next;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 异步/定时的处理器，主要用于及时释放缓存的资源
     */
    static class LogCacheTask implements Runnable {

        private static final Logger log = LoggerFactory.getLogger(LogCacheTask.class);

        private List<LogCache> logCaches = new LinkedList<>();  //
        private List<LogCache> recycledLogCaches = new ArrayList<>();   // 被回收的缓存对象
        private long scheduleTime;  // 周期时间

        private LogCacheTask(long scheduleTime) {
            this.scheduleTime = scheduleTime;
        }

        private void addLogCache(LogCache logCache) {
            logCaches.add(logCache);
        }

        @Override
        public void run() {
            for (;;) {
                try {
                    synchronized (LogCacheTask.class) {
                        LogCacheTask.class.wait(scheduleTime);
                    }
                } catch (InterruptedException e) {
                    log.warn("LogCache task interrupted for {}, {}", e, e.getMessage());
                }
                recycleSchedule();
            }
        }

        /**
         * 定时回收
         */
        private void recycleSchedule() {
            try {
                log.info("LogCache recycle schedule executing...");
                long currentTime = System.currentTimeMillis();
                for (LogCache logCache : logCaches) {
                    long cacheTime = logCache.getCacheTime();
                    if (cacheTime > 0 && cacheTime <  currentTime - logCache.lastUseTime) {
                        logCache.recycleSchedule();
                        recycledLogCaches.add(logCache);
                    }
                }
            } finally {
                for (LogCache recycledLogCache : recycledLogCaches) logCaches.remove(recycledLogCache);

                log.info("LogCache recycle schedule success ! recycled size is {}", recycledLogCaches.size());
                recycledLogCaches.clear();
            }
        }

        private void recycle() {

        }

        private void start() {
            Thread thread = new Thread(this, "LogCacheTask");
            thread.setPriority(3);
            thread.setDaemon(true);
            thread.start();
            log.info("LogCache task inited !");
        }

        private void anotify() {
            synchronized (LogCacheTask.class) {
                LogCacheTask.class.notify();
            }
        }
    }
}
