package org.mot.common.cache;

import org.mot.common.annotation.Cacheable;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;




public class CacheModule extends AbstractModule{

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		
		//bind(CacheManager.class).toInstance(CacheFactory.getCacheManager());
		//CacheInterceptor ci = new CacheInterceptor();
		//requestInjection(ci);
		//requestStaticInjection(CacheInterceptor.class);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Cacheable.class), new CacheInterceptor());
	}

}
