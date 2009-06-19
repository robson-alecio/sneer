package spikes.bamboo.mocotoh;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JUnitExpectationsTest {
	
	public Object _instance;
	
	@Test
	public void test1() {
		assertNull(_instance);
		_instance = new Object();
	}
	
	@Test
	public void test2() {
		assertNull(_instance);
		_instance = new Object();
	}
	
}
