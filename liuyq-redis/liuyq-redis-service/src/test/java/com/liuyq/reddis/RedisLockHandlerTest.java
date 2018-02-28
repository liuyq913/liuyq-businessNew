package com.liuyq.reddis;

import com.liuyq.redisLock.IRedisLockHandler;
import com.liuyq.redisLock.RedisLockHandler;
import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.TimeUnit;

/**
 * Created by liuyq on 2017/11/14.
 */
public class RedisLockHandlerTest {
    @Test
    public void test(){
        //创建jdeis池配置实例
        JedisPoolConfig config = new JedisPoolConfig();
        //设置常规配置
        config.setMaxTotal(1024);
        config.setMaxIdle(200);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        JedisPool pool = new JedisPool(config,"192.168.100.26", 6379);
        IRedisLockHandler lockHandler = new RedisLockHandler(pool);
        if(lockHandler.tryLock("abcd",20, TimeUnit.SECONDS)){
            System.out.println("get lock .....");
        }else {
            System.out.println("not get lock....");
        }
        lockHandler.unLock("abcd");
    }
}
