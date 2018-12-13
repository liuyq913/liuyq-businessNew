package com.liuyq.log2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/4.20:56
 * @see
 */
public class PooledByteBuffer {

    private static final Logger log = LoggerFactory.getLogger(PooledByteBuffer.class);

    /**
     * 回收器，PooledByteBuffer的回收/获取工作由回收器完成
     */
    private static final Recycler<PooledByteBuffer> RECYCLER = new Recycler<PooledByteBuffer>() {
        @Override
        protected PooledByteBuffer newObject(Handler handler) {
            return new PooledByteBuffer(handler, 0);
        }
    };

    private final Recycler.Handler handler;

    private int maxCapacity;    // 最大容量，用于扩展
    private ByteBuffer buffer;

    private PooledChunk<ByteBuffer> chunk;
    private long handle;
    private ByteBuffer memory;
    int offset; // 当前ByteBuffer在memory中的偏移量
    private int len;
    private int maxLen;
    private int recycleNum; // test
    private int allocNum; // test

    PooledByteBuffer(Recycler.Handler handler, int maxCapacity) {
        this.handler = handler;
    }

    public <T> void init(PooledChunk<T> chunk, long handle, int offset, int len, int maxLen) {
        assert handle >= 0;
        assert chunk != null;

        this.chunk = (PooledChunk<ByteBuffer>) chunk;
        this.handle = handle;
        memory = (ByteBuffer) chunk.memory;
        this.offset = offset;
        this.len = len;
        this.maxLen = maxLen;
        //log.info("PooledByteBuffer {} alloc nums is {}", this, ++allocNum);
    }

    void initUnpooled(PooledChunk<ByteBuffer> chunk, int length) {
        assert chunk != null;

        this.chunk = chunk;
        handle = 0;
        memory = chunk.memory;
        offset = 0;
        this.len = maxLen = length;
    }

    public static PooledByteBuffer newInstance(int maxCapacity) {
        PooledByteBuffer buffer = RECYCLER.get();
        buffer.maxCapacity = maxCapacity;
        return buffer;
    }

    /**
     * 释放该{@link PooledByteBuffer}占用的内存空间
     */
    public void recycle() {
        if (handle >= 0) {
            final long handle = this.handle;
            this.handle = -1;
            memory = null;
            buffer = null;
            chunk.recycle(handle, maxLen);
            doRecycle();
        }
        //log.info("PooledByteBuffer {} recycle, num is {}", this, ++recycleNum);
    }

    private void doRecycle() {
        Recycler.Handler recyclerHandler = this.handler;
        if (recyclerHandler != null) {
            RECYCLER.recycle(this, recyclerHandler);
        }
    }

    public ByteBuffer buffer() {
        if (buffer == null) {
            memory.limit(offset + len).position(offset);
            buffer = memory.slice();
        }
        return buffer;
    }

    public void copy(PooledByteBuffer srcBuffer) {
        try {
            ByteBuffer buffer = buffer();
            ByteBuffer srcBuf = srcBuffer.buffer();
            buffer.put(srcBuf.array(), srcBuffer.offset,
                    (srcBuf.limit() == srcBuf.capacity()) ? srcBuf.position() : srcBuf.limit() - srcBuf.position());    // 不使用flip()
        } catch (Exception e) {
            log.warn("Exception {}, {} with PooledByteBuffer copy by : srcBuffer-{}, limit-{}, remaining-{}, position-{}",
                    e, e.getMessage(), srcBuffer, buffer.limit(), buffer.remaining(), buffer.position());
            throw e;
        }
    }

    public void copy(PooledByteBuffer srcBuffer, int offset, int len) {
        try {
            ByteBuffer buffer = buffer();
            buffer.put(srcBuffer.buffer().array(), srcBuffer.offset + offset, len);
        } catch (Exception e) {
            log.warn("Exception {}, {} with PooledByteBuffer copy by : srcBuffer-{}, limit-{}, remaining-{}, position-{}, len-{}",
                    e, e.getMessage(), srcBuffer, buffer.limit(), buffer.remaining(), buffer.position(), len);
            throw e;
        }
    }

    public PooledByteBuffer flip() {
        ByteBuffer buffer = buffer();
        if (buffer.limit() == buffer.capacity()) buffer.flip();
        return this;
    }

    public void prepWrite() {
        ByteBuffer buffer = buffer();
        buffer.position(buffer.limit()).limit(buffer.capacity());
    }

    @Override
    public String toString() {
        return "PooledByteBuffer{" +
                "chunk=" + chunk +
                ", handle=" + handle +
                ", memory=" + memory +
                ", offset=" + offset +
                ", len=" + len +
                ", maxLen=" + maxLen +
                '}';
    }
}
