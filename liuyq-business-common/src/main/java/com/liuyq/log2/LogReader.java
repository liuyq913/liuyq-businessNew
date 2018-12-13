package com.liuyq.log2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/4.20:24
 * @see
 */
public class LogReader {

    private static final Logger log = LoggerFactory.getLogger(LogReader.class);

    public static final int COMPLETE = 0;
    public static final int SUCCESS = 1;
    public static final int REMOVE = 2;

    @Resource
    protected LogConfig logConfig;  // 日志阅读器配置

    private Map<String, LogCacheWrapper> cacheWrappers;  // 全局配置，根据配置信息，部署多个LogCache

    /**
     * 初始化工作，根据{@link LogConfig}的信息，配置初始化数据
     */
    @PostConstruct
    public void init() {
        try {
            if (cacheWrappers != null) return;

            cacheWrappers = new HashMap<>(2);
            Map<String, String> logPaths = logConfig.getLogPaths();
            logPaths.forEach((name, path) -> cacheWrappers.put(name, createLogCacheWrapper(path)));
            doInit();

            log.info("Log Reader inited !");
        } catch (Exception e) {
            log.warn("Log Reader init failed for {}, {}", e, e.getMessage());
        }
    }

    /**
     * 创建全局{@link LogCache}
     * @param logPath 日志路径
     * @return
     */
    private LogCacheWrapper createLogCacheWrapper(String logPath) {
        ThreadLocalCache logCache = null;
        try {
            LogFile logFile = new LogFile(logPath);
            logCache = new ThreadLocalCache(this, logConfig, logFile, logPath, false);
            // 初始化
            logFile.init(logConfig, logCache);
            logCache.setThreadLocal(new ThreadLocal<>());
            long fileSize = logFile.getFileSize();
            logCache.setLastPos(fileSize);
            logCache.setLastFileSize(fileSize);
            logCache.setLastModifyTime(logFile.getLastModifyTime());
            doInitLogCache(logCache);
            return new LogCacheWrapper(logCache);
        } catch (Exception e) {
            log.warn("Log Cache {} init failed for {}, {}", logCache == null ? null : logCache, e, e.getMessage());
        }
        return null;
    }

    /**
     * 用于自定义
     * @param logCache
     */
    protected void doInitLogCache(LogCache logCache) {
    }

    protected void doInit() {
    }

    protected ThreadLocalCache getThreadLocalCache(int line, String path) throws IOException {
        // 获取全局缓存，有且未被使用，则复用
        LogCacheWrapper cacheWrapper = getGlobalLogCache(path);
        if (cacheWrapper == null) cacheWrapper = reuseLogCacheWrapper(path);
        ThreadLocalCache cache = (ThreadLocalCache) cacheWrapper.cache;
        ThreadLocalCache localCache = cache.getThreadLocalCache();    // 获取线程缓存，优先使用
        if (localCache != null) return localCache;
        if (cacheWrapper.updateUse()) return cache;
        // 没有当前线程的缓存且全局缓存被使用，则创建一个当前线程的缓存，并将全局缓存的数据拷贝到当前缓存
        return createSubThreadLogCache(cache);
    }

    private LogCacheWrapper reuseLogCacheWrapper(String path) throws IOException {
        String logPath = logConfig.getLogPaths().get(path);
        if (logPath == null) {  // 使用默认机制
            logPath = logConfig.getLogDirPath() + "/" + path + logConfig.getLogSuffix();
        }
        return createLogCacheWrapper(logPath);
    }

    private ThreadLocalCache createSubThreadLogCache(ThreadLocalCache cache) throws IOException {
        try {
            LogFile logFile = new LogFile(cache.getLogPath());
            ThreadLocalCache threadLocalCache = cache.createSubCache(this, logConfig, logFile, cache.getLogPath());
            logFile.init(logConfig, threadLocalCache);
            copyValue(cache, threadLocalCache);
            doInitLogCache(threadLocalCache);
            return threadLocalCache;
        } catch (IOException e) {
            log.warn("Log Cache subCache init failed for {}, {}", e, e.getMessage());
            throw e;
        }
    }

    private void copyValue(ThreadLocalCache src, ThreadLocalCache dst) {
        dst.setLastPos(src.getLastPos());
        dst.setLastFileSize(src.getLastFileSize());
        dst.setLastModifyTime(src.getLastModifyTime());
        dst.setLastLine(src.getLastLine());
        // 复制数据
        PooledByteBuffer lineBuffer = dst.getLineBuffer();
        lineBuffer.copy(src.getLineBuffer());
        ByteWrapper lineByteWrapper = dst.getLineByteWrapper();
        lineByteWrapper.copy(src.getLineByteWrapper());
        log.info("ThreadLocalCache copy and inited !");
    }

    /**
     * 获取path对应的全局{@link LogCache}
     * @param path
     * @return
     */
    private LogCacheWrapper getGlobalLogCache(String path) {
        return cacheWrappers.get(path);
    }

    protected String getLogPath(String path) throws IOException {
        LogCacheWrapper cacheWrapper = cacheWrappers.get(path);
        String logPath = cacheWrapper == null ? null : cacheWrapper.cache.getLogPath();
        if (logPath == null) {
            logPath = logConfig.getLogPaths().get(path);
            if (logPath == null) logPath = logConfig.getLogDirPath() + "/" + path + logConfig.getLogSuffix();
        }
        return logPath;
    }

    public String getGlobalLogCacheByPath(String path) {
        return getGlobalLogCache(path).cache.toString();
    }

    /**
     * 计算pos
     * @param lastPos
     * @param initPos
     * @param readBufferSize
     * @return
     */
    protected long position(long lastPos, long initPos, int readBufferSize) {
        if (lastPos - initPos < readBufferSize) {
            return initPos;
        } else {
            return lastPos - readBufferSize;
        }
    }

    /**
     * 从前向后解析日志，将日志解析为行
     * @param pooledBuffer
     * @param lastPos
     * @param pos
     * @param parser
     */
    protected void parseLineHead(PooledByteBuffer pooledBuffer, long lastPos, long pos, Parser parser) {
        ByteBuffer buffer = pooledBuffer.buffer();
        buffer.flip();

        int size = buffer.remaining(), internal = 0, idx = 0;  // internal=偏移量
        int posCounter = 0, lineCounter = 0, len;
        boolean lined = false;
        for (int i = 0; i < size; i++) {
            byte chr = buffer.get(i);
            if ('\r' == chr || '\n' == chr) {
                lined = true;
                internal ++;
                if (i == size - 1) {    // 末行解析
                    parser.init(pooledBuffer, size, idx, i - idx, lineCounter, ++posCounter); // 不计入行计数器，当前值要计入
                    parser.parseTail();
                }
            } else {
                if (lined) {
                    len = i - internal - idx;
                    if (len > 0) {
                        parser.init(pooledBuffer, size, idx, i - idx, ++lineCounter, posCounter);
                        parser.parse();
                        idx = i;
                        posCounter = 0;
                    }
                    lined = false;
                    internal = 0;
                }
            }
            posCounter ++;
        }
    }

    /**
     * 从后向前解析日志，将日志解析为行
     * @param pooledBuffer
     * @param lastPos
     * @param pos
     * @param parser
     */
    protected void parseLineTail(PooledByteBuffer pooledBuffer, long lastPos, long pos, Parser parser) {
        ByteBuffer buffer = pooledBuffer.buffer();
        buffer.flip();  // 设置为可读
        int size = Math.min(buffer.remaining(), (int) (lastPos - pos)), internal = 0, idx = size - 1;  // internal，换行符的偏移量
        log.debug("Log Reader buffer size is {}", size);
        int posCounter = 0, lineCounter = 0;
        boolean lined = false;
        for (int i = size - 1; i >= 0; i--) {   // 从后向前解析
            byte chr = buffer.get(i);
            if ('\r' == chr || '\n' == chr) {
                lined = true;
                internal ++;
            } else {
                if (lined) {
                    int ii = i + internal;
                    if (idx - ii > 0) {
                        parser.init(pooledBuffer, size, ii + 1, idx - ii, ++lineCounter, posCounter - internal);  // 需要减掉前面的"\r\n"
                        parser.parse();
                        posCounter = internal;  // 加上后面"\r\n"
                    }
                    idx = i + internal; // 在"\r\n"位置标记，等于加上后面的"\r\n"
                    lined = false;
                    internal = 0;
                } else if (i == 0) {    // 末行处理，末行和其它行不一样，哎
                    parser.init(pooledBuffer, size, i, idx - i + 1, lineCounter, ++posCounter); // 不计入行计数器，当前值要计入
                    parser.parseTail();
                }
            }
            posCounter ++;
        }
    }

    /**
     * 用于测试
     * @param buffer
     * @param offset
     * @param len
     */
    private void test(ByteBuffer buffer, int offset, int len) {
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = buffer.get(offset + i);
        }

        log.info(new String(bytes));
    }

    /**
     * 回收/释放，不参与定时回收
     */
    public void recycle() {
        cacheWrappers.forEach((path, cacheWrapper) -> cacheWrapper.recycle());
    }

    /**
     * {@link LogReader}子类的全局缓存的包装类，主要用于保证缓存不被同时使用
     */
    class LogCacheWrapper {

        LogCache cache;

        private AtomicBoolean using;    // 是否被使用
        private WeakReference<LogUser> user;    // cache的持有这

        LogCacheWrapper(LogCache cache) {
            this.cache = cache;
            this.using = new AtomicBoolean(false);
        }

        /**
         * CAS
         * @return
         */
        public boolean updateUse() {
            if (using.compareAndSet(false, true)) {
                user = new WeakReference<>(LogUser.LogThreadUser.get(Thread.currentThread()));
                return true;
            }
            return false;
        }

        void recycle() {
            // 校验
            if (using.get() && Thread.currentThread().equals(user.get().user)) {
                using.compareAndSet(true, false);
                //cache.recycle();
            }
        }
    }

    /**
     * 日志信息的解析器
     */
    protected abstract class Parser {

        protected PooledByteBuffer pooledBuffer;
        protected int size; // 本次读的有效数据的大小
        protected int offset; // 行在ByteBuffer中偏移量
        protected int len;    // 行的字节长度
        protected int lineCounter;    // 行计数器
        protected long posCounter;    // 位置计数器，每解析一行重置一次

        /**
         * 按行调用
         */
        void init(PooledByteBuffer pooledBuffer, int size, int offset, int len, int lineCounter, long posCounter) {
            this.pooledBuffer = pooledBuffer;
            this.size = size;
            this.offset = offset;
            this.len = len;
            this.lineCounter = lineCounter;
            this.posCounter = posCounter;
        }

        /**
         * 按行调用
         */
        protected abstract void parse();

        protected void parseTail() {    // 末行解析
        }

        /**
         * 校验容量
         * @param srcSize
         * @param newSize
         * @param pooledBuffer
         * @return
         */
        protected boolean checkCapacity(int srcSize, int newSize, PooledByteBuffer pooledBuffer) {
            if (newSize == 0 || srcSize + newSize > pooledBuffer.buffer().capacity()) return false;
            return true;
        }
    }
}
