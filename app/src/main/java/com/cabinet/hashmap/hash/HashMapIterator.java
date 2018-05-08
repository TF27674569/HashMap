package com.cabinet.hashmap.hash;

import java.security.Key;
import java.util.NoSuchElementException;

/**
 * Description :
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/7
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class HashMapIterator<K, V> implements Map.Iterator<Map.Entry<K, V>> {
    // 当前是第几个
    int index;
    // 表的位置
    int tableIndex;
    // 集合
    HashMap<K, V> hashMap;
    // 当前对象
    HashMap.Entry current;

    HashMapIterator(HashMap<K, V> hashMap) {
        this.hashMap = hashMap;
        HashMap<K, V>.Entry<K, V>[] table = hashMap.getTable();
        while (tableIndex < table.length && (current = table[++tableIndex]) == null) ;
    }

    /**
     * 是否存在下一个元素
     */
    @Override
    public boolean hasNext() {
        return index < hashMap.size();
    }

    /**
     * 获取下一个元素
     */
    @Override
    public Map.Entry<K, V> next() {
        // 临时存储next
        HashMap.Entry e = current;

        // 当这一个链表遍历完毕之后
        if ((current = e.next) == null) {
            HashMap<K, V>.Entry<K, V>[] table = hashMap.getTable();
            // 找下一条链表
            while (tableIndex < table.length && (current = table[tableIndex++]) == null) ;
        }

        index++;
        return e;
    }

}
