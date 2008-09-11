package sneer.kernel.container.tests;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.impl.SimpleContainer;
import sneer.kernel.container.impl.classloader.MetaClassClassLoader;
import sneer.kernel.container.utils.metaclass.ClassUtils;
import sneer.kernel.container.utils.metaclass.MetaClass;
import sneer.pulp.deployer.Deployer;

public class PerformanceTest {

	@Test
	public void testClassLoading() throws Exception {
		Container c1 = new SimpleContainer();
		Container c2 = new SimpleContainer();
		Object o1 = c1.produce(Deployer.class);
		Object o2 = c2.produce(Deployer.class);
		ClassLoader cl1 = o1.getClass().getClassLoader();
		ClassLoader cl2 = o2.getClass().getClassLoader();
		assertNotSame(cl1, cl2);
	}


	@Test
	public void testClassLoaderCache() throws Exception {
		MetaClass meta = ClassUtils.metaClass(Deployer.class);
		List<MetaClass> files = new ArrayList<MetaClass>();
		files.add(meta);
		ClassLoader cl = new MetaClassClassLoader(files, this.getClass().getClassLoader());
		Class<?> clazz = cl.loadClass(Deployer.class.getName());
		assertSame(cl, clazz.getClassLoader());
		Class<?> clazz2 = cl.loadClass(Deployer.class.getName());
		assertSame(clazz, clazz2);
	}

}
