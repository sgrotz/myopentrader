package org.mot.common.cache;

public interface CacheWrapper<K, V> {

	  void put(K key, V value);
	  
	  int put(V value);

	  V get(K key);
	
}
