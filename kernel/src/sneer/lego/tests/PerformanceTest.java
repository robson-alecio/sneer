package sneer.lego.tests;

import static org.junit.Assert.assertNotSame;

import org.apache.commons.lang.ObjectUtils;
import org.junit.Test;

import sneer.lego.Container;
import sneer.lego.impl.SimpleContainer;

public class PerformanceTest {

	@Test
	public void testClassLoading() throws Exception {
		Container c1 = new SimpleContainer();
		Container c2 = new SimpleContainer();
		Object o1 = c1.produce("sneer.bricks.deployer.Deployer");
		Object o2 = c2.produce("sneer.bricks.deployer.Deployer");
		ClassLoader cl1 = o1.getClass().getClassLoader();
		ClassLoader cl2 = o2.getClass().getClassLoader();
		assertNotSame(cl1, cl2);
		System.out.println(ObjectUtils.identityToString(cl1));
		System.out.println(ObjectUtils.identityToString(cl2));
	}
}
