package com.cabinet.hashmap.hash;

import java.util.HashSet;
import java.util.Set;

/**
 * Description : 手写hashmap核心代码
 * <p>
 * Created : TIAN FENG
 * Date : 2018/5/7
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class HashMap<K, V> implements Map<K, V> {

    // 默认大小
    private static int sDefaultLen = 1 << 4;
    // 扩容因子
    private static float sDefaultLoadFactor = 0.75f;

    // 表大小
    private Entry<K, V>[] table;

    // 扩容因子
    private float loadFactor;

    // 当前长度
    private int useSize;


    public HashMap() {
        this(sDefaultLen, sDefaultLoadFactor);
    }


    public HashMap(int len, float loadFactor) {
        //这里判断2的n次方
        if (len < 0 || (len & (len - 1)) != 0) {
            throw new IllegalArgumentException("len 必须是 2 的整数次幂");
        }

        if (loadFactor <= 0) {
            throw new IllegalArgumentException("扩容因子必须大于0");
        }

        this.loadFactor = loadFactor;

        table = new Entry[len];
    }

    /**
     * 添加值
     *
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        // 如果槽点数 达到扩容点
        if (useSize > table.length * loadFactor) {
            // 进行扩容 2倍长度
            reSize(table.length << 1);
        }

        // 处理null值
        if (key == null) {
            putForNullKey(value);
            return;
        }

        // 不用扩容
        int index = indexFor(key, table.length);

        // 添加到链表中
        addEntry(key, value, index);

    }

    /**
     * 添加到链表
     */
    private void addEntry(K key, V value, int index) {

        // 拿到对应位置的entry
        Entry<K, V> entry = table[index];

        // 表示第一次进来 之前没有数据
        if (entry == null) {
            table[index] = new Entry<>(key, value, null);
            // 大小+1
            useSize++;
            return;
        }

        // 处理重复的key值
        while (entry != null) {
            if (entry.k != null// 防止空指针
                    && entry.k.hashCode() == key.hashCode() // hash值一样
                    && ((entry.k) == key || key.equals(entry.k)))/* key对象一样*/ {
                // key 一样就将value替换掉
                entry.v = value;
                return;
            }
            entry = entry.next;
        }
        // 不为null 将新的entry放在链表头
        // 并将entry的next对象置为之前的entry
        table[index] = new Entry<>(key, value, table[index]);
        // 大小+1
        useSize++;
    }

    /**
     * 处理null值
     */
    private void putForNullKey(V value) {

        // 拿到 0 号位 table
        Entry<K, V> e = table[0];

        // 不为null
        while (e != null) {
            // 去遍历key为bull的值
            if (e.k == null) {
                // 为null 直接替换掉value
                e.v = value;
                return;
            }
            // 下一个继续判断
            e = e.next;
        }

        // 如果所有的都没有遍历到null的key

        // 拿到头位置
        e = table[0];

        // 重置头位置
        table[0] = new Entry<>(null, value, e);

        useSize++;
    }

    /**
     * 确定hash碰撞后所在的槽点位置
     */
    private int indexFor(K key, int length) {
        // 保证二进制的值为 11111...
        int i = length - 1;
        // hash(key.hashCode())&i 保证碰撞后的槽点落在 table上
        return hash(key) & i;
    }

    /**
     * hash散列算法 降低碰撞概率
     */
    private int hash(K key) {
        int h = key == null ? 0 : key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * 扩容
     */
    private void reSize(int size) {
        // 新的entrt数组
        Entry<K, V>[] newTable = new Entry[size];

        // 扩容时需要将老的数组进行重新散列
        for (Entry<K, V> e : table) {

            // 散列链表上的各个元素
            while (null != e) {
                // 1.拿到下标对应的值 第二次循环需要
                Entry<K, V> next = e.next;
                // 2. 计算散列位置
                int i = indexFor(e.k, size);
                // 3. entry的next指向newTable链表上一个之前的元素 也就是将旧的entry往下压
                e.next = newTable[i];
                // 4. 将这个entry放在链表头
                newTable[i] = e;
                // 5. 重复下一个
                e = next;
            }
        }

        // 扩容之后重置表
        table = newTable;
    }

    /**
     * 获取值
     *
     * @param key
     */
    @Override
    public V get(K key) {

        // 处理null key
        if (key == null) {
            return getNullKey();
        }

        Entry<K, V> e = getEntry(key);

        return null == e ? null : e.v;
    }

    /**
     * 处理空key
     */
    private V getNullKey() {
        int i = indexFor(null, table.length);
        Entry<K, V> e = table[i];
        while (e != null) {
            if (e.k == null) {
                return e.v;
            }
        }
        return null;
    }

    /**
     * 获取Entry
     */
    private Entry<K, V> getEntry(K key) {

        // 无数据直接null
        if (useSize == 0) {
            return null;
        }

        // indexFor 我处理的null值所以 这里不用管null
        int index = indexFor(key, table.length);
        for (Entry<K, V> e = table[index];// entry
             e != null;// 不为null
             e = e.next) {
            if (e.k != null// 防止空指针
                    && e.k.hashCode() == key.hashCode() // hash值一样
                    && ((e.k) == key || key.equals(e.k)))// key对象一样
                return e;
        }

        return null;
    }

    /**
     * 槽点数量
     */
    @Override
    public int size() {
        return useSize;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HashMapIterator<>(this);
    }


    Entry<K, V>[] getTable() {
        return table;
    }

    /**
     * hash map的entry
     *
     * @param <K>
     * @param <V>
     */
     class Entry<K, V> implements Map.Entry<K, V> {

        K k;
        V v;
        Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            k = key;
            v = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return k;
        }

        @Override
        public V getValue() {
            return v;
        }
    }

}
