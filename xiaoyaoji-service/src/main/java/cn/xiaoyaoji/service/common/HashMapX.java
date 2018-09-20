package cn.xiaoyaoji.service.common;

import java.util.HashMap;

/**
 * @author: zhoujingjie
 * @Date: 16/5/2
 */
public class HashMapX<K,V> extends HashMap<K,V> {

    public HashMapX<K,V> append(K key, V value){
        put(key,value);
        return this;
    }
}
