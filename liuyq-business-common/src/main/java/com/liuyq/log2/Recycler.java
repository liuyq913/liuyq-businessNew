package com.liuyq.log2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * <p>
 *     对对象T进行缓存，减少对象的创建回收频率，减少gc。主要应用对象为{@link PooledByteBuffer}。
 *     可以理解为轻量级多功能对象池。
 * </p>
 * 在{@link com.lccx.core.cache.CacheUtil.PooledExpireCache}中也使用了对象池技术，两者的实现区别较大。
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/6.10:31
 * @see
 */
public abstract class Recycler<T> {

    private static final Logger log = LoggerFactory.getLogger(Recycler.class);

    private static final int DEFAULT_MAX_CAPACITY;
    private static final int INITIAL_CAPACITY;  // 初始容量
    private static final int OWN_ID = Integer.MIN_VALUE;

    static {
        DEFAULT_MAX_CAPACITY = 512;
        INITIAL_CAPACITY = 32;
        log.info("PooledByteBuffer Recycler initCapactiy is {} and maxCapacity is {}", INITIAL_CAPACITY, DEFAULT_MAX_CAPACITY);
    }

    private final int maxCapacity;
    private final Stack<T> stack;   // 用于存储对象的栈

    protected Recycler() {
        this(DEFAULT_MAX_CAPACITY);
    }

    protected Recycler(int maxCapacity) {
        this.maxCapacity = Math.max(0, maxCapacity);
        this.stack = new Stack<>(this, maxCapacity);
    }

    /**
     * 生成对象，可定制
     * @param handler
     * @return
     */
    protected abstract T newObject(Handler handler);

    /**
     * 获取或者新增一个对象
     * @return
     */
    public T get() {
        DefaultHandler handler = stack.pop();
        if (handler == null) {
            handler = stack.newHandler();
            handler.value = newObject(handler);
        }
        return (T) handler.value;
    }

    /**
     * 回收对象，入栈
     * @param o
     * @param handler
     * @return
     */
    public boolean recycle(T o, Handler handler) {
        DefaultHandler h = (DefaultHandler) handler;
        if (h.stack.parent != this) {   // 校验
            return false;
        }
        if (o != h.value) {
            throw new IllegalArgumentException("o does not belong to handle");
        }

        h.recycle();
        return true;
    }

    interface Handler {

    }

    static class DefaultHandler implements Handler {

        private int lastRecycledId;
        private int recycleId;

        private Stack<?> stack; // 持有该hander的栈
        private Object value;

        DefaultHandler(Stack<?> stack) {
            this.stack = stack;
        }

        public void recycle() {
            stack.push(this);
        }
    }

    /**
     * 使用栈结构存储目标对象，进行缓存
     * @param <T>
     */
    static class Stack<T> {

        final Recycler<T> parent;
        private DefaultHandler[] elements;
        private final int maxCapacity;  // 栈的最大容量
        private int size;   // elements使用的大小，可以理解为最后一个DefaultHander元素的下标

        public Stack(Recycler<T> parent, int maxCapacity) {
            this.parent = parent;
            this.maxCapacity = maxCapacity;
            this.elements = new DefaultHandler[Math.min(INITIAL_CAPACITY, maxCapacity)];
            log.info("PooledByteBuffer Recycler stack capacity is {}", elements.length);
        }

        /**
         * 取出栈顶元素
         * @return
         */
        public DefaultHandler pop() {
            int size = this.size;
            if (size == 0) {
                /*if (! scavenge()) {   // 暂不需要
                    return null;
                }*/
                return null;
                //size = this.size;
            }
            size --;    // 下标-1
            DefaultHandler ret = elements[size];
            if (ret.lastRecycledId != ret.recycleId) {  // 被回收多次
                throw new IllegalStateException("recycled multiple times");
            }
            ret.recycleId = 0;
            ret.lastRecycledId = 0;
            this.size = size;
            return ret;
        }

        /**
         * 入栈
         * @param item
         */
        public void push(DefaultHandler item) {
            if ((item.recycleId | item.lastRecycledId) != 0) {
                throw new IllegalStateException("recycled already");
            }
            item.recycleId = item.lastRecycledId = OWN_ID;

            int size = this.size;
            if (size >= maxCapacity) {
                return;
            }
            if (size == elements.length) {
                elements = Arrays.copyOf(elements, Math.min(size << 1, maxCapacity));   // 扩大elements
                log.info("PooledByteBuffer Recycler stack capacity is {}", elements.length);
            }

            elements[size] = item;
            this.size = size + 1;
        }

        DefaultHandler newHandler() {
            return new DefaultHandler(this);
        }
    }
}
