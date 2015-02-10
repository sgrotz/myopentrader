package org.mot.common.cache;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.mot.common.annotation.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CacheInterceptor implements MethodInterceptor {
	
	private CacheFactory cf = CacheFactory.getInstance();
	private Logger logger = LoggerFactory.getLogger(getClass());
	
		
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		String cacheName = invocation.getMethod().getAnnotation(Cacheable.class).name();
		int ttl = invocation.getMethod().getAnnotation(Cacheable.class).ttl();
		
		if (!cf.doesCacheExist(cacheName)) {
			cf.createCache(cacheName);
			logger.debug("Created new cache ..." + cacheName);
		}
		
		
		String args = convertArgumentsToStringKey(invocation.getArguments());
		
		String key = (invocation.getMethod().getName()) + (args);
		Object o = null;
		
		logger.debug("Looking for " + key + " in cache " + cacheName);
		
		if (!cf.doesCacheEntryExist(cacheName, key)) {
			o = invocation.proceed();
			cf.addCacheEntry(cacheName, key, o, ttl);
		} else {
			o = cf.getCacheEntry(cacheName, key);
		}
		
		return o;
	}
	
	private String convertArgumentsToStringKey(Object[] arg) {
		String ret = "";
		for (int i = 0; i < arg.length; i++) {
			ret = ret + "_" + arg[i].toString();
		}
		return ret;
		
	}
	
	


}
