package com.liuyq.log2;

/**
 * <P>
 *     类似于二次缓存，{@link PooledChunk}中计算出来的内存偏移量产生的{@link PooledByteBuffer}的值被缓存在该类中，
 *     再次获取时，可以减少计算时间。
 * </P>
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/5.16:43
 * @see
 */
public abstract class MemoryRegionCache<T> {

    private final Entry<T>[] entries;
    private final int maxUnusedCached;
    private int head;
    private int tail;
    private int maxEntriesInUse;
    private int entriesInUse;

    MemoryRegionCache(int size) {
        entries = new Entry[powerOfTwo(size)];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new Entry<>();
        }
        maxUnusedCached = size / 2;
    }

    private static int powerOfTwo(int res) {
        if (res <= 2) {
            return 2;
        }
        res--;
        res |= res >> 1;
        res |= res >> 2;
        res |= res >> 4;
        res |= res >> 8;
        res |= res >> 16;
        res++;
        return res;
    }

    public boolean allocate(PooledByteBuffer buffer, int initCapacity) {
        Entry<T> entry = entries[head];
        if (entry.chunk == null) {
            return false;
        }

        entriesInUse ++;
        if (maxEntriesInUse < entriesInUse) {
            maxEntriesInUse = entriesInUse;
        }
        initBuf(entry.chunk, entry.handle, buffer, initCapacity);
        // only null out the chunk as we only use the chunk to check if the buffer is full or not.
        entry.chunk = null;
        head = nextIdx(head);
        return true;
    }

    protected abstract void initBuf(PooledChunk<T> chunk, long handle, PooledByteBuffer buffer, int initCapacity);

    private int nextIdx(int index) {
        // use bitwise operation as this is faster as using modulo.
        return index + 1 & entries.length - 1;
    }

    public boolean add(PooledChunk<T> chunk, long handle) {
        Entry<T> entry = entries[tail];
        if (entry.chunk != null) {
            // cache is full
            return false;
        }
        entriesInUse --;

        entry.chunk = chunk;
        entry.handle = handle;
        tail = nextIdx(tail);
        return true;
    }

    private static final class Entry<T> {
        PooledChunk<T> chunk;
        long handle;
    }

    static final class SubPageMemoryRegionCache<T> extends MemoryRegionCache<T> {

        SubPageMemoryRegionCache(int size) {
            super(size);
        }

        @Override
        protected void initBuf(PooledChunk<T> chunk, long handle, PooledByteBuffer buffer, int initCapacity) {
            chunk.initBufWithSubpage(buffer, handle, initCapacity);
        }
    }

    static final class NormalMemoryRegionCache<T> extends MemoryRegionCache<T> {

        NormalMemoryRegionCache(int size) {
            super(size);
        }

        @Override
        protected void initBuf(PooledChunk<T> chunk, long handle, PooledByteBuffer buffer, int initCapacity) {
            chunk.initBuf(buffer, handle, initCapacity);
        }
    }
}
