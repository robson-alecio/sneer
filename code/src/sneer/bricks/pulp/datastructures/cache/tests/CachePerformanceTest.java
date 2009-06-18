package sneer.bricks.pulp.datastructures.cache.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.pulp.datastructures.cache.Cache;
import sneer.bricks.pulp.datastructures.cache.CacheFactory;
import sneer.foundation.brickness.testsupport.BrickTest;

public class CachePerformanceTest extends BrickTest{

	private final Cache<Object> _subject= my(CacheFactory.class).createWithCapacity(3000);
	
	@Test (timeout = 500) 
	public void containsPerformance() {
		populate();
		
		Object obj= new Object();
		for(int i=1; i<=3000;i++) 
			_subject.contains(obj);
	}

	@Test (timeout = 2000) 
	public void handleForPerformance() {
		populate();
		
		Object obj = new Object();
		for(int i=1; i<=3000;i++) 
			_subject.handleFor(obj);
	}

	private void populate() {
		for(int i=1; i<=3000;i++) 
			_subject.keep(new Object());
	}

}
