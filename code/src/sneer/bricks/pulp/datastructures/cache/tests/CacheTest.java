package sneer.bricks.pulp.datastructures.cache.tests;

import static sneer.foundation.commons.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.pulp.datastructures.cache.Cache;
import sneer.bricks.pulp.datastructures.cache.CacheFactory;
import sneer.foundation.brickness.testsupport.BrickTest;

public class CacheTest extends BrickTest{
	
	private final Cache<Object> _subject= my(CacheFactory.class).createWithCapacity(3);

	@Test
	public void happyDayForCache(){
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		Object o4 = new Object();
		
		assertFalse(_subject.contains(o1));
		assertFalse(_subject.contains(o2));
		assertFalse(_subject.contains(o3));
		assertFalse(_subject.contains(o4));
		_subject.keep(o1);
		_subject.keep(o2);
		_subject.keep(o3);
		assertTrue(_subject.contains(o1));
		assertTrue(_subject.contains(o2));
		assertTrue(_subject.contains(o3));

		assertHandle(o1);
		assertHandle(o2);
		assertHandle(o3);
		
		_subject.keep(o4);
		assertFalse(_subject.contains(o1));
		assertTrue(_subject.contains(o2));
		assertTrue(_subject.contains(o3));
		assertTrue(_subject.contains(o4));

		assertHandle(o2);
		assertHandle(o3);
		assertHandle(o4);
	}

	@Test
	public void discardOldObjects(){
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		Object o4 = new Object();
		
		_subject.keep(o1);
		_subject.keep(o2);
		_subject.keep(o3);
		_subject.keep(o1);
		_subject.keep(o1);

		_subject.keep(o4);
		assertTrue(_subject.contains(o1));
		assertFalse(_subject.contains(o2));
		assertTrue(_subject.contains(o3));
		assertTrue(_subject.contains(o4));
	}

	private void assertHandle(Object obj) {
		int handle = _subject.handleFor(obj);
		assertEquals(obj, _subject.getByHandle(handle));
	}

	@Test
	public void unknownHandles() {
		assertNull(_subject.getByHandle(42));
		assertNull(_subject.getByHandle(-42));
	}
	
}
