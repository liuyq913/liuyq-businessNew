package com.liuyq.common.threadpoolexecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 *
 * @author Chen, Rongliang
 * @since p2p_cloud_v1.0
 */
public final class ThreadPoolExecutorUtil
{
    private final static Logger logger = LoggerFactory.getLogger(ThreadPoolExecutorUtil.class);

    private static ThreadPoolExecutor threadPool;

    private ThreadPoolExecutorUtil()
    {
    }

    /**
     * 
     * 初始化线程池
     * 
     * @since p2p_cloud_v1.0
     */

    static
    {
        int corePoolSize = 10;

        int maximumPoolSize = 100;

        long keepAliveTime = 3;

        int workQueue = 10;


        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(workQueue), new ThreadPoolExecutor.DiscardOldestPolicy());
        logger.info("创建线程池成功");
    }

    public synchronized static ThreadPoolExecutor getPool()
    {
        return threadPool;
    }

}
