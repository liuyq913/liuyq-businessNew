package com.liuyq.zookeeper.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * Created by liuyq on 2018/10/24.
 *
 * 基于zookeeper，curator实现的分布式锁
 * InterProcessReadWriteLock
 */
public class InterProcessReadWriteLockTest {
    public static void main(String[] args) throws Exception {
        //创建zookeeper客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();

        //指定锁路径
        String lockPath = "/zkLockRoot/lock_1";
        //创建读写锁
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client, lockPath);

        //生成线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        Consumer<InterProcessMutex> consumer = (InterProcessMutex typeLock)->{
            try{
                List<Callable<String>> callList = new ArrayList<>();
                Callable<String> call = () -> {
                    try{
                        typeLock.acquire();
                        System.out.println(Thread.currentThread() + "  acquire read lock");
                    }catch (Exception e){
                    }finally {
                        typeLock.release();
                        System.out.println(Thread.currentThread() + "  release read lock");
                    }
                    return "true";
                };
                //5个并发线程
                for (int i = 0; i < 5; i++) {
                    callList.add(call);
                }
                List<Future<String>> futures = executor.invokeAll(callList);
            }catch (Exception e){

            }
        };

        //读锁测试(多个线程可同时获取读锁)
        System.out.println("5个并发线程,读锁测试");
        InterProcessMutex readLock = lock.readLock();
        consumer.accept(readLock); //来获取锁

        //写锁测试（同时只有一个线程获取写锁）
        System.out.println("5个并发线程,写锁测试");
        InterProcessMutex writeLock = lock.writeLock();
        consumer.accept(writeLock);

        executor.shutdown();
    }
}
