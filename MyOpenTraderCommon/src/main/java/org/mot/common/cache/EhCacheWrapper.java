package org.mot.common.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EhCacheWrapper<K, V> implements CacheWrapper<K, V> {

	
    private final String cacheName;
    private final CacheManager cacheManager;
	
    public EhCacheWrapper(final String cacheName, final CacheManager cacheManager)
    {
        this.cacheName = cacheName;
        this.cacheManager = cacheManager;
    }
    
	@Override
	public void put(K key, V value) {
		// TODO Auto-generated method stub
		
		getCache().put(new Element(key, value));
	}

	@Override
	public V get(K key) {
		// TODO Auto-generated method stub
        Element element = getCache().get(key);
        if (element != null) {
            return (V) element.getObjectValue();
        }
        return null;
	}

    public Ehcache getCache() 
    {
        return cacheManager.getEhcache(cacheName);
    }

	@Override
	public int put(V value) {
		// TODO Auto-generated method stub
		
		int hash = value.hashCode();
		getCache().put(new Element(hash, value));
		
		return hash;
	}

	
}
