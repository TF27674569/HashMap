# HashMap源码解析与手写实现

## **一、成员变量**
&nbsp;　　1.扩容因子
```java
static final float DEFAULT_LOAD_FACTOR = 0.75f;
```
&nbsp;　　&nbsp;　　这个值是经过试验得出的一个合理的值，这个值过大，查询，插入会比较耗时，过小，会频繁扩容  
  
  &nbsp;　　2.长度
 ```java
 static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
```
&nbsp;　　&nbsp;　　必须使用2的整数次幂

## **二、put函数系列操作**
```java
 public V put(K key, V value) {
         if (table == EMPTY_TABLE) {
             // 创建Entry数组
             inflateTable(threshold);
         }
         
         // 可以添加null值的key
         if (key == null)
             return putForNullKey(value);
         // 经过一系列的hash散列算法 减少碰撞概率
         int hash = hash(key);
         // 提出单独分析
         int i = indexFor(hash, table.length);
         // 处理值
         for (Entry<K,V> e = table[i]; e != null; e = e.next) {
             Object k;
             // 存在同样的值 直接替换
             if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                 V oldValue = e.value;
                 e.value = value;
                 e.recordAccess(this);
                 return oldValue;
             }
         }
 
         modCount++;
         // 添加进链表
         addEntry(hash, key, value, i);
         return null;
     }
     
 hash 和 indexFor
 
 // 散列算法降低碰撞概率
  final int hash(Object k) {
      
         h ^= k.hashCode();
         h ^= (h >>> 20) ^ (h >>> 12);
         return h ^ (h >>> 7) ^ (h >>> 4);
     }
 
     // 这里就很好的解析了为什么length必须是2的整数次幂
     // length-1 为奇数 任何的奇数的二进制都是1111111（2*n-1）
     // 而任何的值与上1111111 都必须是0到1111111 之间
     // 防止tab[indexFor()]角标溢出 
     // 这个地方经过hash减少碰撞在与上length-1就几乎平均分配在table上了
  static int indexFor(int h, int length) {
         return h & (length-1);
     }
```
## **三、resize函数**
```java
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

```
## **四、HashMap结构**

HashMap采用数组+链表的结构能有效的提高存储查询的效率但是在空间结构上比较复杂

