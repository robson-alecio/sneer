package sneer.lego.tests;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sneer.lego.Container;
import sneer.lego.impl.SimpleContainer;
import sneer.lego.impl.classloader.MetaClassClassLoader;
import sneer.lego.utils.metaclass.ClassUtils;
import sneer.lego.utils.metaclass.MetaClass;

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
		//System.out.println(ObjectUtils.identityToString(cl1));
		//System.out.println(ObjectUtils.identityToString(cl2));
	}


	@Test
	public void testClassLoaderCache() throws Exception {
		String className = "sneer.bricks.deployer.Deployer";
		MetaClass meta = ClassUtils.metaClass(className);
		List<MetaClass> files = new ArrayList<MetaClass>();
		files.add(meta);
		ClassLoader cl = new MetaClassClassLoader(files, this.getClass().getClassLoader());
		Class<?> clazz = cl.loadClass(className);
		assertSame(cl, clazz.getClassLoader());
		Class<?> clazz2 = cl.loadClass(className);
		assertSame(clazz, clazz2);
	}

}
