package com.cabinet.hashmap.hash;

import java.util.Set;

/**
 * Description : map 抽象类
 * <p/>
 * Created : TIAN FENG
 * Date : 2018/5/7
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface Map<K,V> {

    /**
     * 添加值
     */
    void put(K key,V value);

    /**
     * 获取值
     */
    V get(K key);

    /**
     * 槽点数量
     */
    int size();

    Iterator<Map.Entry<K,V>> iterator();


    interface Entry<K,V>{

        K getKey();

        V getValue();
    }

    /**
     * Entry 的迭代器
     */
    public interface Iterator<T extends Map.Entry>{

        /**
         * 是否存在下一个元素
         */
        public boolean hasNext();

        /**
         * 获取下一个元素
         */
        public T next();
    }

}
