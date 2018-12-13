package com.liuyq.log2;

import java.lang.ref.WeakReference;

/**
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/5.11:02
 * @see
 */
public class ThreadLocalCache extends LogCache {

    private ThreadLocalCache parent;    // 全局缓存对象

    private ThreadLocal<ThreadLocalCache> threadLocal;  // ThreadLocalCache的缓存，一般只有初始化的ThreadLocalCache需要赋值
    private WeakReference<Thread> threadRef;
    Long threadTime;   // ThreadLocal缓存超时时间

    public ThreadLocalCache(LogReader logReader, LogConfig logConfig, LogFile logFile, String logPath) {
        super(logReader, logConfig, logFile, logPath, true);
    }

    public ThreadLocalCache(LogReader logReader, LogConfig logConfig, LogFile logFile, String logPath, boolean addToTask) {
        super(logReader, logConfig, logFile, logPath, addToTask);
    }

    public void setThreadLocal(ThreadLocal<ThreadLocalCache> threadLocal) {
        this.threadLocal = threadLocal;
    }

    public Thread getThread() {
        return threadRef == null ? null : threadRef.get();
    }

    public void setThread(Thread thread) {
        this.threadRef = new WeakReference<>(thread);
    }

    public void setThread() {
        setThread(Thread.currentThread());
    }

    public void setParent(ThreadLocalCache parent) {
        this.parent = parent;
    }

    public ThreadLocalCache createSubCache(LogReader logReader, LogConfig logConfig, LogFile logFile, String logPath) {
        return createSubCache(logReader, logConfig, logFile, logPath, true);
    }

    public ThreadLocalCache createSubCache(LogReader logReader, LogConfig logConfig, LogFile logFile, String logPath, boolean addToTask) {
        ThreadLocalCache subCache = new ThreadLocalCache(logReader, logConfig, logFile, logPath, addToTask);
        threadLocal.set(subCache);
        subCache.setThread(Thread.currentThread());
        subCache.setParent(this);
        return subCache;
    }

    public ThreadLocalCache getThreadLocalCache() {
        return threadLocal.get();
    }

    public long getThreadTime() {
        if (threadTime == null) threadTime = logConfig.getThreadTime();
        return threadTime;
    }

    @Override
    protected long getCacheTime() {
        return getThreadTime();
    }

    @Override
    protected void doRecycleSchedule() {
        parent.threadLocal.remove();
    }
}
