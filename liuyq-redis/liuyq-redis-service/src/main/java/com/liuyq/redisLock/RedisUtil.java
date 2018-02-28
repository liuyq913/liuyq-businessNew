package com.liuyq.redisLock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by liuyq on 2017/11/16.
 */
public class RedisUtil {
    /**
     * 数据源
     */
    private ShardedJedisPool shardedJedisPool;

    /**
     * 执行器 保证执行完后释放资源
     * @param <T>
     */
    abstract class Executor<T>{
         ShardedJedis shardedJedis;

        ShardedJedisPool shardedJedisPool;

        public Executor(ShardedJedisPool shardedJedisPool){
            this.shardedJedisPool = shardedJedisPool;
            this.shardedJedis = shardedJedisPool.getResource();
        }
        /**
         * 回调
         * @return
         */
        abstract T execute();

        /***
         *调用{@link #execute()}并返回执行结果
         * @return
         */
        public T getReuslt(){
            T result = null;
            try {
                result = execute();
            }catch (Throwable e) {
                throw new RuntimeException("Redis execute exception", e);
            }
            finally {
                if(shardedJedis != null){
                    shardedJedis.close();
                }
            }
            return result;
        }
    }

    /**
     *  删除模糊匹配的key
     @param likeKey 模糊匹配的key
     @return 删除成功的条数
     */
    public long delKeysLike(final String likeKey){
        return new Executor<Long>(shardedJedisPool) {
            @Override
            Long execute() {
                Collection<Jedis> jedisC = shardedJedis.getAllShards();
                Iterator iterator = jedisC.iterator();
                long count = 0;
                while(iterator.hasNext()){
                    Jedis _jedis = (Jedis) iterator.next();
                    Set<String> keys = _jedis.keys(likeKey+"*");
                    count+=_jedis.del(keys.toArray(new String[keys.size()]));
                }
                return count;
            }
        }.getReuslt();
    }

    /**
     * 删除匹配到的key
     */
    public long delKey(final String key){
       return new Executor<Long>(shardedJedisPool) {
           @Override
           Long execute() {
              return shardedJedis.del(key);
           }
       }.getReuslt();
    }
    /**
     * 删除匹配的key的集合
     * 返回删除成功的条数
     */
    public long delKeys(final String[] keys){
        return new Executor<Long>(shardedJedisPool) {
            @Override
            Long execute() {
                long count = 0;
                Collection<Jedis> jedisC = shardedJedis.getAllShards();
                Iterator iterator = jedisC.iterator();
                while(iterator.hasNext()){
                    Jedis jedis = (Jedis) iterator.next();
                    count += jedis.del(keys);
                }
                return count;
            }
        }.getReuslt();
    }

    /**
     * 删除指定Key中指定的field
     * @param key
     * @param keys
     * @return
     */
    public long hdelFields(final String key, final String... keys){
        return new Executor<Long>(shardedJedisPool) {
            @Override
            Long execute() {
                return shardedJedis.hdel(key, keys);
            }
        }.getReuslt();
    }

    /**
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     * 在 Redis 中，带有生存时间的 key 被称为『可挥发』(volatile)的。
     * @param key
     * @param expire 生命周期，单位为秒
     * @return 1: 设置成功 0: 已经超时或key不存在
     */
    public long expire(final String key, final int expire){
        return new Executor<Long>(shardedJedisPool) {
            @Override
            Long execute() {
                return shardedJedis.expire(key, expire);
            }
        }.getReuslt();
    }

    /**
     * 一个跨jvm的id生成器，利用了redis原子性操作的特点
     * @return
     */
    public long makeId(final String key){
        return new Executor<Long>(shardedJedisPool) {
            @Override
            Long execute() {
                long id = shardedJedis.incr(key);
                if((id + 75807)>=Long.MAX_VALUE){
                // 避免溢出，重置，getSet命令之前允许incr插队，75807就是预留的插队空间
                    shardedJedis.getSet(key,"0"); //设置key的并返回key的旧值
                }
                return id;
            }
        }.getReuslt();
    }
}
