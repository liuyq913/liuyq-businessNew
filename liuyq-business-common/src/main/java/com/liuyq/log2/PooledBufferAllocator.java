package com.liuyq.log2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * <P>内存分配</P>
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/4.20:53
 * @see
 */
public class PooledBufferAllocator {

    private static final Logger log = LoggerFactory.getLogger(PooledBufferAllocator.class);

    private static final int MAX_CHUNK_SIZE = (int) (((long) Integer.MAX_VALUE + 1) / 2);

    public static final PooledBufferAllocator DEFAULT = new PooledBufferAllocator();    // 默认分配器

    private final PooledChunk<ByteBuffer> chunk;
    private final int maxOrder;
    final int pageSize;
    final int chunkSize;
    final int pageShifts;

    final int subpageOverflowMask;  // 超过pageSize大小的标记
    private final PooledSubpage<ByteBuffer>[] smallSubpagePools;
    private final int smallSubpagePoolNum;  //

    public PooledBufferAllocator() {
        this(4096, 10);
    }

    public PooledBufferAllocator(int pageSize, int maxOrder) {
        this.pageSize = pageSize;
        this.maxOrder = maxOrder;
        this.chunkSize = validateAndCalculateChunkSize(pageSize, maxOrder);
        this.pageShifts = validateAndCalculatePageShifts(pageSize);
        this.subpageOverflowMask = ~(pageSize - 1); // 相当于~pageSize + 1，反码+1，pageSize取反

        this.chunk = newChunk(pageSize, maxOrder, pageShifts, chunkSize);

        smallSubpagePoolNum = pageShifts - 9;
        smallSubpagePools = new PooledSubpage[smallSubpagePoolNum];
        for (int i = 0; i < smallSubpagePools.length; i++) {
            smallSubpagePools[i] = newSubpagePoolHead(pageSize);
        }
        log.info("PooledBuffer inited !");
        log.info("PooledBuffer pageSize is {}", pageSize);
        log.info("PooledBuffer chunkSize is {}", chunkSize);
    }

    private int validateAndCalculateChunkSize(int pageSize, int maxOrder) {
        if (maxOrder > 14) {
            throw new IllegalArgumentException("maxOrder: " + maxOrder + " (expected: 0-14)");
        }

        int chunkSize = pageSize;   // 初始值
        for (int i = maxOrder; i > 0; i--) {
            if (chunkSize > MAX_CHUNK_SIZE / 2) {
                throw new IllegalArgumentException(String.format(
                        "pageSize (%d) << maxOrder (%d) must not exceed %d", pageSize, maxOrder, MAX_CHUNK_SIZE));
            }
            chunkSize <<= 1;
        }

        return chunkSize;
    }

    private int validateAndCalculatePageShifts(int pageSize) {
        if ((pageSize & pageSize - 1) != 0) {
            throw new IllegalArgumentException("pageSize: " + pageSize + " (expected: power of 2)");
        }

        return Integer.SIZE - 1 - Integer.numberOfLeadingZeros(pageSize);
    }

    private PooledChunk<ByteBuffer> newChunk(int pageSize, int maxOrder, int pageShifts, int chunkSize) {
        ByteBuffer buffer = ByteBuffer.allocate(chunkSize); // 一次性分配
        return new PooledChunk<>(this, buffer, pageSize, maxOrder, pageShifts, chunkSize);
    }

    private PooledSubpage<ByteBuffer> newSubpagePoolHead(int pageSize) {
        PooledSubpage<ByteBuffer> head = new PooledSubpage<>(pageSize);
        head.prev = head;
        head.next = head;
        return head;
    }

    public PooledByteBuffer buffer(int initCapacity, int maxCapacity) {
        assert this.chunk != null;

        return allocate(initCapacity, maxCapacity);
    }

    private PooledByteBuffer allocate(int initCapacity, int maxCapacity) {
        PooledByteBuffer buffer = PooledByteBuffer.newInstance(maxCapacity);    // 初始化一个空的buffer
        allocate(initCapacity, buffer); // 分配
        return buffer;
    }

    /**
     * 分配内存
     * @param initCapacity
     * @param buffer
     */
    private void allocate(int initCapacity, PooledByteBuffer buffer) {
        final int normCapacity = normCapacity(initCapacity);
        if (isSmall(normCapacity)) {    // capacity < pagesize
            int tableIdx;
            PooledSubpage<ByteBuffer>[] table;
            if (chunk.allocateSmall(buffer, initCapacity, normCapacity)) {
                return;
            }
            // 分配失败处理
            tableIdx = chunk.smallIdx(normCapacity);
            table = smallSubpagePools;

            synchronized (this) {   // 相当于重新创建
                final PooledSubpage<ByteBuffer> head = table[tableIdx];
                final PooledSubpage<ByteBuffer> s = head.next;
                if (s != head) {
                    assert s.doNotDestroy && s.elemSize == normCapacity;
                    long handle = s.allocate();
                    assert handle >= 0;
                    s.chunk.initBufWithSubpage(buffer, handle, initCapacity);
                    return;
                }
            }
        } else if (normCapacity <= chunkSize) {
            if (chunk.allocateNormal(buffer, initCapacity, normCapacity)) {
                return;
            }
        } else {
            allocateHuge(buffer, initCapacity);
            return;
        }
        allocateNormal(buffer, initCapacity, normCapacity); // 第一次初始分配
    }

    boolean isSmall(int normCapacity) {
        return (normCapacity & subpageOverflowMask) == 0;
    }

    /**
     * TODO
     * @param initCapacity
     * @return
     */
    private int normCapacity(int initCapacity) {
        return initCapacity;
    }

    /**
     * 初始分配，第一次分配都会经过这个方法
     * @param buffer
     * @param initCapacity
     * @param normCapacity
     */
    private synchronized void allocateNormal(PooledByteBuffer buffer, int initCapacity, int normCapacity) {
        PooledChunk<ByteBuffer> c = chunk;
        long handle = c.allocate(normCapacity); // 获取handle，前32为表示在二叉树中的位置，后32位表示在subpage中的位置
        assert handle > 0;
        c.initBuf(buffer, handle, initCapacity);
    }

    private void allocateHuge(PooledByteBuffer buffer, int initCapacity) {
        buffer.initUnpooled(newUnpooledChunk(initCapacity), initCapacity);
    }

    protected PooledChunk<ByteBuffer> newUnpooledChunk(int capacity) {
        return new PooledChunk<>(this, ByteBuffer.allocate(capacity), capacity);
    }

    public PooledSubpage findSubpagePoolHead(int elemSize) {
        int tableIdx;
        PooledSubpage[] table;
        tableIdx = 0;
        elemSize >>>= 10;
        while (elemSize != 0) {
            elemSize >>>= 1;
            tableIdx ++;
        }
        table = smallSubpagePools;
        return table[tableIdx];
    }
}
