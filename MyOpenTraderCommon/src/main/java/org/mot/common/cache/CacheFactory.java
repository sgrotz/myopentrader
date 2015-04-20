package org.mot.common.cache;


import org.apache.log4j.PropertyConfigurator;
import org.mot.common.math.TechnicalAnalysis;
import org.mot.common.tools.PropertiesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheFactory {

	private PropertiesFactory pf = PropertiesFactory.getInstance();
	private String pathToConfigDir = pf.getConfigDir();
	private CacheManager manager;
	private static CacheFactory cf;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private CacheFactory() {
		// Make sure to initialize the cachemanager
		manager = CacheManager.newInstance(pathToConfigDir + "/ehcache.xml");
	}
	
	public static CacheFactory getInstance() {
		if (cf == null) {
			cf = new CacheFactory();
		}
		return cf;
	}
	
	
	public void shutdown() {
		manager.shutdown();
	}
	
	
	public void createCache(String cacheName) {
		logger.debug("Creating new Cache: " + cacheName);
		manager.addCache(cacheName);
	}
	
	
	public boolean doesCacheExist(String cacheName) {
		logger.debug("Checking if Cache: " + cacheName + " exists ...");
		return manager.cacheExists(cacheName);
	}
	
	public boolean doesCacheEntryExist(String cacheName, String key) {
		logger.debug("Checking if Cache entry: " + key + " in cache " + cacheName + " exists ...");
		Cache cache = manager.getCache(cacheName);
		
		return cache.isKeyInCache(key);
	}
	
	public void addCacheEntry(String cacheName, String key, Object value, int ttl) {
		logger.debug("Adding new element to Cache: " + cacheName + " with key: " + key + " and ttl " + ttl);
		Cache cache = manager.getCache(cacheName);
		Element e = new Element(key, value);
		if (ttl > 0) {
			e.setTimeToLive(ttl);
		}
		cache.put(e);
		
	}
	
	public Object getCacheEntry(String cacheName, String key) {
		logger.debug("Getting element with key: " + key + " from Cache: " + cacheName);
		Cache cache = manager.getCache(cacheName);
		Element e = cache.get(key);
		
		if (e != null) {
			return e.getObjectValue();
		} else {
			return null;
		}
		
	}
	
	
	
	
	
	public static void main(String[] args) {
		
		PropertiesFactory pf = PropertiesFactory.getInstance();
		pf.setConfigDir(args[0]);

		System.setProperty("PathToConfigDir", pf.getConfigDir());
		PropertyConfigurator.configure(pf.getConfigDir() + "/log4j.properties");
		        
        //Injector injector = Guice.createInjector(new CacheModule());
        //TechnicalAnalysis service = injector.getInstance(TechnicalAnalysis.class);
        
		Guice.createInjector(new CacheModule());
        TechnicalAnalysis service =  TechnicalAnalysis.getInstance();
        
        
		long start = System.currentTimeMillis();
		
		service.getSeriesByDays("AAPL", 3, true);
		int result;
		System.out.println("Iteration 1 took: " + (System.currentTimeMillis() - start) + " msecs");
		
		
		
		start = System.currentTimeMillis();
		service.getSeriesByDays("AAPL", 3, true);
		System.out.println("Iteration 2 (cached) took: " + (System.currentTimeMillis() - start) + " msecs");

		
	}
	
}
