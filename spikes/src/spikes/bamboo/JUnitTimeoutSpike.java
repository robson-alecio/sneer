package spikes.bamboo;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class JUnitTimeoutSpike {
	
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
	
	@Before
	public void before() {
		System.out.println("before");
	}
	
	@Test(timeout=500)
	public void test() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
		}
	}
}
