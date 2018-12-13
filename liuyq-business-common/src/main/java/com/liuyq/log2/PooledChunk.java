package com.liuyq.log2;

/**
 * <P>
 *     基于完全平衡二叉树结构
 * </P>
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/5.16:09
 * @see
 */
public class PooledChunk<T> {

    PooledBufferAllocator parent;

    T memory;   // 实际的内存映射

    private final int pageSize;     // 二叉树叶子结点的值大小
    private final int pageShifts;
    private final int maxOrder;     // 二叉树最大深度
    private final int chunkSize;    // chunk或memory的大小
    private final int log2ChunkSize;
    private final byte unusable;    // chunk中二叉树节点不可用的标志，该值表示节点不可用
    private final int subpageOverflowMask;
    private final int maxSubpageAllocs; // 二叉树叶子节点的数量
    private final byte[] memoryMap; // 表示二叉树每个节点的可用性，分配数组对内存使用来说是最优的
    private final byte[] depthMap;  // 表示二叉树每个节点的深度映射

    private final MemoryRegionCache<T>[] smallSubPageCaches;
    private final MemoryRegionCache<T>[] normalHeapCaches;
    private final int shiftsNormalHeapNum;
    private final PooledSubpage<T>[] subpages;  // subpage，数量不可变

    public PooledChunk(PooledBufferAllocator parent, T memory, int pageSize, int maxOrder, int pageShifts, int chunkSize) {
        this.parent = parent;
        this.memory = memory;
        this.pageSize = pageSize;
        this.pageShifts = pageShifts;
        this.maxOrder = maxOrder;
        this.chunkSize = chunkSize;
        this.log2ChunkSize = log2(chunkSize);
        this.unusable = (byte) (maxOrder + 1);  // 最大深度+1
        this.subpageOverflowMask = ~(pageSize - 1);
        this.maxSubpageAllocs = 1 << maxOrder;
        this.shiftsNormalHeapNum = log2(pageSize);

        memoryMap = new byte[maxSubpageAllocs << 1];
        depthMap = new byte[memoryMap.length];
        int memoryMapIndex = 1; // 树节点从1开始
        for (int d = 0; d <= maxOrder; ++d) { //
            int depth = 1 << d; // 深度从1开始
            for (int p = 0; p < depth; ++ p) {  // 相同深度的值是一致的
                memoryMap[memoryMapIndex] = (byte) d;   // 给每个节点映射初始化赋值，都小于unusable，都可用
                depthMap[memoryMapIndex] = (byte) d;
                memoryMapIndex ++;
            }
        }

        subpages = newSubpageArray(maxSubpageAllocs);
        smallSubPageCaches = createSubPageCaches(256, pageShifts - 9);
        normalHeapCaches = createNormalCaches(64, 32 * 1024, this);
    }

    PooledChunk(PooledBufferAllocator parent, T memory, int size) {
        this.parent = parent;
        this.memory = memory;
        memoryMap = null;
        depthMap = null;
        subpages = null;
        subpageOverflowMask = 0;
        pageSize = 0;
        pageShifts = 0;
        maxOrder = 0;
        unusable = (byte) (maxOrder + 1);
        chunkSize = size;
        log2ChunkSize = log2(chunkSize);
        maxSubpageAllocs = 0;

        smallSubPageCaches = null;
        normalHeapCaches = null;
        shiftsNormalHeapNum = 0;
    }

    private PooledSubpage<T>[] newSubpageArray(int size) {
        return new PooledSubpage[size];
    }

    private MemoryRegionCache.SubPageMemoryRegionCache<T>[] createSubPageCaches(int cacheSize, int numCaches) {
        if (cacheSize > 0) {
            MemoryRegionCache.SubPageMemoryRegionCache<T>[] cache = new MemoryRegionCache.SubPageMemoryRegionCache[numCaches];
            for (int i = 0; i < cache.length; i++) {
                cache[i] = new MemoryRegionCache.SubPageMemoryRegionCache<>(cacheSize);
            }
            return cache;
        } else {
            return null;
        }
    }

    private static <T> MemoryRegionCache.NormalMemoryRegionCache<T>[] createNormalCaches(
            int cacheSize, int maxCachedBufferCapacity, PooledChunk chunk) {
        if (cacheSize > 0) {
            int max = Math.min(chunk.chunkSize, maxCachedBufferCapacity);
            int arraySize = Math.max(1, max / chunk.pageSize);

            MemoryRegionCache.NormalMemoryRegionCache<T>[] cache = new MemoryRegionCache.NormalMemoryRegionCache[arraySize];
            for (int i = 0; i < cache.length; i++) {
                cache[i] = new MemoryRegionCache.NormalMemoryRegionCache<T>(cacheSize);
            }
            return cache;
        } else {
            return null;
        }
    }

    public boolean allocateSmall(PooledByteBuffer buffer, int initCapacity, int normCapacity) {
        return allocate(cacheForSmall(normCapacity), buffer, initCapacity);
    }

    private MemoryRegionCache cacheForSmall(int normCapacity) {
        int idx = smallIdx(normCapacity);
        return cache(smallSubPageCaches, idx);
    }

    static int smallIdx(int normCapacity) {
        int tableIdx = 0;
        int i = normCapacity >>> 10;    // /1024
        while (i != 0) {
            i >>>= 1;
            tableIdx ++;
        }
        return tableIdx;
    }

    private MemoryRegionCache<T> cache(MemoryRegionCache<T>[] cache, int idx) {
        if (cache == null || idx > cache.length - 1) {
            return null;
        }
        return cache[idx];
    }

    public boolean allocateNormal(PooledByteBuffer buffer, int initCapacity, int normCapacity) {
        return allocate(cacheForNormal(normCapacity), buffer, initCapacity);
    }

    private MemoryRegionCache<T> cacheForNormal(int normCapacity) {
        int idx = log2(normCapacity >> shiftsNormalHeapNum);
        return cache(normalHeapCaches, idx);
    }

    void initBuf(PooledByteBuffer buffer, long handle, int reqCapacity) {
        int memoryMapIdx = (int) handle;
        int bitmapIdx = (int) (handle >>> Integer.SIZE);
        if (bitmapIdx == 0) {
            byte val = value(memoryMapIdx);
            assert val == unusable : String.valueOf(val);
            buffer.init(this, handle, runOffset(memoryMapIdx), reqCapacity, runLength(memoryMapIdx));
        } else {
            initBufWithSubpage(buffer, handle, bitmapIdx, reqCapacity);
        }
    }

    void initBufWithSubpage(PooledByteBuffer buffer, long handle, int reqCapacity) {
        initBufWithSubpage(buffer, handle, (int) (handle >>> Integer.SIZE), reqCapacity);
    }

    private void initBufWithSubpage(PooledByteBuffer buffer, long handle, int bitmapIdx, int reqCapacity) {
        assert bitmapIdx != 0;

        int memoryMapIdx = (int) handle;

        PooledSubpage<T> subpage = subpages[subpageIdx(memoryMapIdx)];
        assert subpage.doNotDestroy;
        assert reqCapacity <= subpage.elemSize;

        buffer.init(
                this, handle,
                runOffset(memoryMapIdx) + (bitmapIdx & 0x3FFFFFFF) * subpage.elemSize, reqCapacity, subpage.elemSize);
    }

    long allocate(int normCapacity) {
        if ((normCapacity & subpageOverflowMask) != 0) { // >= pageSize
            return allocateRun(normCapacity);
        } else {
            return allocateSubpage(normCapacity);
        }
    }

    private long allocateRun(int normCapacity) {
        int d = maxOrder - (log2(normCapacity) - pageShifts);
        int id = allocateNode(d);
        if (id < 0) {
            return id;
        }
        //freeBytes -= runLength(id);
        return id;
    }

    /**
     * Create/ initialize a new PoolSubpage of normCapacity
     * Any PoolSubpage created/ initialized here is added to subpage pool in the PoolArena that owns this PoolChunk
     *
     * @param normCapacity normalized capacity
     * @return index in memoryMap
     */
    private long allocateSubpage(int normCapacity) {
        int d = maxOrder; // subpages are only be allocated from pages i.e., leaves
        int id = allocateNode(d);
        if (id < 0) {
            return id;
        }

        final PooledSubpage<T>[] subpages = this.subpages;
        final int pageSize = this.pageSize;

        //freeBytes -= pageSize;

        int subpageIdx = subpageIdx(id);
        PooledSubpage<T> subpage = subpages[subpageIdx];
        if (subpage == null) {
            subpage = new PooledSubpage<>(this, id, runOffset(id), pageSize, normCapacity);
            subpages[subpageIdx] = subpage;
        } else {
            subpage.init(normCapacity);
        }
        return subpage.allocate();
    }

    private int allocateNode(int d) {
        int id = 1;
        int initial = - (1 << d); // has last d bits = 0 and rest all = 1
        byte val = value(id);
        if (val > d) { // unusable
            return -1;
        }
        while (val < d || (id & initial) == 0) { // id & initial == 1 << d for all ids at depth d, for < d it is 0
            id <<= 1;
            val = value(id);
            if (val > d) {
                id ^= 1;
                val = value(id);
            }
        }
        byte value = value(id);
        assert value == d && (id & initial) == 1 << d : String.format("val = %d, id & initial = %d, d = %d",
                value, id & initial, d);
        setValue(id, unusable); // mark as unusable
        updateParentsAlloc(id);
        return id;
    }

    private boolean allocate(MemoryRegionCache cache, PooledByteBuffer buffer, int initCapacity) {
        if (cache == null) {
            return false;
        }
        boolean allocated = cache.allocate(buffer, initCapacity);
        /*if (++ allocations >= freeSweepAllocationThreshold) {
            allocations = 0;
            trim();
        }*/
        return allocated;
    }

    private int subpageIdx(int memoryMapIdx) {
        return memoryMapIdx ^ maxSubpageAllocs; // remove highest set bit, to get offset
    }

    private void updateParentsAlloc(int id) {
        while (id > 1) {
            int parentId = id >>> 1;
            byte val1 = value(id);
            byte val2 = value(id ^ 1);
            byte val = val1 < val2 ? val1 : val2;
            setValue(parentId, val);
            id = parentId;
        }
    }

    private void updateParentsFree(int id) {
        int logChild = depth(id) + 1;
        while (id > 1) {
            int parentId = id >>> 1;
            byte val1 = value(id);
            byte val2 = value(id ^ 1);
            logChild -= 1; // in first iteration equals log, subsequently reduce 1 from logChild as we traverse up

            if (val1 == logChild && val2 == logChild) {
                setValue(parentId, (byte) (logChild - 1));
            } else {
                byte val = val1 < val2 ? val1 : val2;
                setValue(parentId, val);
            }

            id = parentId;
        }
    }

    private byte value(int id) {
        return memoryMap[id];
    }

    private void setValue(int id, byte val) {
        memoryMap[id] = val;
    }

    private byte depth(int id) {
        return depthMap[id];
    }

    private static int log2(int val) {
        // compute the (0-based, with lsb = 0) position of highest set bit i.e, log2
        return Integer.SIZE - 1 - Integer.numberOfLeadingZeros(val);
    }

    private int runLength(int id) {
        // represents the size in #bytes supported by node 'id' in the tree
        return 1 << log2ChunkSize - depth(id);
    }

    private int runOffset(int id) {
        // represents the 0-based offset in #bytes from start of the byte-array chunk
        int shift = id ^ 1 << depth(id);
        return shift * runLength(id);
    }

    void recycle(long handle, int normCapacity) {
        // 加入MemoryRegionCache后，就不需要更新二叉树了，提高性能
        if (add(handle, normCapacity)) {
            return;
        }
        //
        int memoryMapIdx = (int) handle;
        int bitmapIdx = (int) (handle >>> Integer.SIZE);

        if (bitmapIdx != 0) { // free a subpage
            PooledSubpage<T> subpage = subpages[subpageIdx(memoryMapIdx)];
            assert subpage != null && subpage.doNotDestroy;
            if (subpage.recycle(bitmapIdx & 0x3FFFFFFF)) {
                return;
            }
        }
        //freeBytes += runLength(memoryMapIdx);
        setValue(memoryMapIdx, depth(memoryMapIdx));
        updateParentsFree(memoryMapIdx);
    }

    /**
     * 复用{@link MemoryRegionCache}
     * @param handle
     * @param normCapacity
     * @return
     */
    boolean add(long handle, int normCapacity) {
        MemoryRegionCache<T> cache;
        if (parent.isSmall(normCapacity)) {
            cache = cacheForSmall(normCapacity);
        } else {
            cache = cacheForNormal(normCapacity);
        }
        if (cache == null) {
            return false;
        }
        return cache.add(this, handle);
    }
}
