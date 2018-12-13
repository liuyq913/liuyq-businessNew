package com.liuyq.log2;

import java.util.Arrays;
import java.util.List;

/**
 * <P>
 *     存储了行数据的{@link PooledByteBuffer}类的包装类，记录了行数据每一行的偏移量信息，通过重置偏移量，可以读取需要的数据。
 *     这种方式减少了数据移动时的拷贝，以及内存的创建销毁，可以提高性能。
 * </P>
 * @author pudding
 * @version 0.1.0
 * @design
 * @date 2018/12/5.10:43
 * @see
 */
public class ByteWrapper {

    private static final int INIT_OFFSET = 1;

    /**{@link ByteWrapper}对象池*/
    private static final Recycler<ByteWrapper> RECYCLER = new Recycler<ByteWrapper>() {
        @Override
        protected ByteWrapper newObject(Handler handler) {
            return new ByteWrapper(null, handler);
        }
    };
    private Recycler.Handler handler;

    PooledByteBuffer pooledBuffer;
    private int offset; // 当前pooledBuffer的偏移量
    private int len;    // 本次输出数据的长度

    private int[] offsetMap;    // 行的偏移量的映射，用下标表示行，值表示偏移量，第0行的偏移量为0
    private int offsetIdx = INIT_OFFSET;  // offsetMap的使用下标，当前下标未被使用，下标-1以使用

    ByteWrapper(PooledByteBuffer pooledBuffer) {
        this.pooledBuffer = pooledBuffer;
        offsetMap = new int[200];   // 默认200行
    }

    private ByteWrapper(PooledByteBuffer pooledBuffer, Recycler.Handler handler) {
        this(pooledBuffer);
        this.handler = handler;
    }

    /**
     * 通过对象池，减少{@link ByteWrapper}的创建和销毁，其中{@link #offsetMap}对内存的消耗还是比较大的。
     * @param pooledBuffer
     * @return
     */
    static ByteWrapper get(PooledByteBuffer pooledBuffer) {
        ByteWrapper byteWrapper = RECYCLER.get();
        byteWrapper.pooledBuffer = pooledBuffer;
        byteWrapper.offsetIdx = INIT_OFFSET;
        return byteWrapper;
    }

    //  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 对外提供的字节数组
     * @return
     */
    public byte[] bytes() {
        return pooledBuffer.buffer().array();
    }

    /**
     * 本次数据输出的偏移量
     * @return
     */
    public int offset() {
        return pooledBuffer.offset + offset;
    }

    public int len() {
        return len;
    }

    /**
     * 计算指定偏移量下指定行数的对应的byte[]/ByteBuffer的偏移量和长度信息，用于输出
     * @param line  行数
     * @param offset 行偏移量
     */
    ByteWrapper calculateOffsetAndLen(int line, int offset) {
        if (line > offsetIdx - 1) line = offsetIdx - 1;

        this.offset = offsetMap[offset];
        this.len = offsetMap[offset + line] - this.offset;
        return this;
    }

    /**
     * 计算从尾部开始指定偏移量下指定行数的对应的byte[]/ByteBuffer的偏移量和长度信息，用于输出
     * @param line  行数
     * @param offset 行偏移量
     */
    ByteWrapper calculateOffsetAndLenTail(int line, int offset) {
        if (line > offsetIdx - 1) line = offsetIdx - 1;

        this.offset = offsetMap[offsetIdx - 1 - line];
        this.len = offsetMap[offsetIdx - 1] - this.offset;
        return this;
    }

    // 重置偏移量映射 /////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 在当前{@link #offsetIdx}后添加偏移量信息
     * @param len 行的长度
     */
    void addOffsetMap(int len) {
        extendOffsetMap(offsetIdx);

        offsetMap[offsetIdx++] = offsetMap[offsetIdx-1] + len;
    }

    /**
     * 在当前{@link #offsetIdx}后批量添加偏移量信息
     * @param lens 行的长度列表
     */
    void addOffsetMapAll(List<Integer> lens) {
        addOffsetMapAll(offsetIdx, lens);
    }

    /**
     * 在指定位置(index<={@link #offsetIdx})批量添加偏移量信息
     * @param index 指定位置 1<=index<={@link #offsetIdx}
     * @param lens 行的长度列表
     */
    void addOffsetMapAll(int index, List<Integer> lens) {
        if (index > offsetIdx || index < INIT_OFFSET)   // 校验index
            throw new IllegalArgumentException("Log ByteWrapper offset index must in (" + INIT_OFFSET + ", " + offsetIdx + ")");

        int size = lens.size();
        extendOffsetMap(offsetIdx + size - 1);

        if (index < offsetIdx) {    // 移动index-offsetIdx之间的偏移量
            int lenSize = 0, idx = 1;
            for (int i = 0; i < size; i++) lenSize += lens.get(i);
            for (int i = offsetIdx + size - 1; i >= index + size; i--) offsetMap[i] = offsetMap[offsetIdx-idx++] + lenSize;
        }

        int lenSize = offsetMap[index-1];
        for (int i = index; i < index + size; i++) offsetMap[i] = lenSize += lens.get(i - index);
        offsetIdx += size;
    }

    /**
     * 在当前{@link #offsetMap}的下标{@link #INIT_OFFSET}位置添加偏移量信息
     * @param len 行的长度
     */
    void addOffsetMapFirst(int len) {
        extendOffsetMap(offsetIdx);

        if (offsetIdx > INIT_OFFSET) {
            int idx = 1;
            for (int i = offsetIdx; i >= INIT_OFFSET + 1; i--) offsetMap[i] = offsetMap[offsetIdx-idx++] + len;
        }
        offsetMap[INIT_OFFSET] = len;
        offsetIdx++;
    }

    /**
     * 在当前{@link #offsetMap}的下标{@link #INIT_OFFSET}位置批量添加偏移量信息
     * @param lens 行的长度列表
     */
    void addOffsetMapFirstAll(List<Integer> lens) {
        int size = lens.size();
        extendOffsetMap(offsetIdx + size - 1);

        if (offsetIdx > INIT_OFFSET) {  // 移动之前的偏移量
            int lenSize = 0, idx = 1;
            for (int i = 0; i < size; i++) lenSize += lens.get(i);
            for (int i = offsetIdx + size - 1; i >= INIT_OFFSET + size; i--) offsetMap[i] = offsetMap[offsetIdx-idx++] + lenSize;
        }
        int lenSize = 0;
        for (int i = INIT_OFFSET; i < INIT_OFFSET + size; i++) offsetMap[i] = lenSize += lens.get(i - INIT_OFFSET);
        offsetIdx += size;
    }

    /**
     * 从头开始删除指定行数的偏移量信息
     * @param line 行数
     */
    void removeOffsetMap(int line) {
        if (line > offsetIdx - 1) line = offsetIdx - 1;
        int removeOffset = offsetMap[line]; // 需要减掉的offset
        for (int i = INIT_OFFSET; i < offsetIdx - line; i++) offsetMap[i] = offsetMap[i+line] - removeOffset;
        offsetIdx -= line;
    }

    /**
     * 删除尾部指定行数的偏移量信息
     * @param line 行数
     */
    void removeOffsetMapLast(int line) {
        if (line > offsetIdx - 1) line = offsetIdx - 1;
        offsetIdx -= line;
    }

    // 获取行信息 /////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 从头获取指定行的偏移量
     * @param line  行数
     * @return
     */
    int getOffset(int line) {
        if (line > offsetIdx - 1) line = offsetIdx - 1;
        return offsetMap[line];
    }

    /**
     * 获取总偏移量，也就是数据总的大小
     * @return
     */
    int getOffsetLast() {
        return getOffset(offsetIdx - 1);
    }

    /**
     * 获取偏移量对应的行数
     * @return
     */
    int getLine() {
        return offsetIdx - 1;
    }

    // 其它 ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 扩展{@link #offsetMap}的大小
     * @param size  当前需要的尺寸
     */
    private void extendOffsetMap(int size) {
        if (offsetMap.length <= size) {    // 扩大2倍，简单粗暴
            int offsetMapLen2 = offsetMap.length << 1;
            if (size > offsetMapLen2) offsetMapLen2 = size + offsetMap.length;

            offsetMap = Arrays.copyOf(offsetMap, offsetMapLen2);
        }
    }

    public void copy(ByteWrapper srcByteWrapper) {
        // 复制offsetMap
        int[] srcOffsetMap = srcByteWrapper.offsetMap;
        int srcOffsetMapLen = srcOffsetMap.length;
        if (offsetMap == null || offsetMap.length < srcOffsetMapLen) {  // 创建新的，先这样
            offsetMap = new int[srcOffsetMapLen];
        }
        System.arraycopy(srcOffsetMap, 0, offsetMap, 0, srcOffsetMapLen);
        // 复制offsetIdx
        offsetIdx = srcByteWrapper.offsetIdx;
    }

    void recycle() {
        RECYCLER.recycle(this, handler);
    }

    @Override
    public String toString() {
        return "ByteWrapper{" +
                "offset=" + pooledBuffer == null ? null : pooledBuffer.offset +
                ", len=" + getOffsetLast() +
                ", offsetMapLength=" + offsetMap.length +
                ", offsetIdx=" + offsetIdx +
                '}';
    }
}
