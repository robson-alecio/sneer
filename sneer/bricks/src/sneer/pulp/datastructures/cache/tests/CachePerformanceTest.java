package sneer.pulp.datastructures.cache.tests;

import static wheel.lang.Environments.my;

import org.junit.Test;

import sneer.pulp.datastructures.cache.Cache;
import sneer.pulp.datastructures.cache.CacheFactory;
import tests.TestInContainerEnvironment;

public class CachePerformanceTest extends TestInContainerEnvironment{

	private final Cache _subject= my(CacheFactory.class).createWithCapacity(3000);
	
	@Test (timeout = 200) 
	public void cachePerformance() {
		for(int i=1; i<=3000;i++) 
			_subject.keep(new Object());
		
		Object obj= new Object();
		for(int i=1; i<=3000;i++) 
			_subject.contains(obj);
	}

}
