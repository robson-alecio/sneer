package spikes.leandro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Ignore;
import org.junit.Test;

public class ClassLoaderTest {

	@Ignore
	@Test
	public void testBrickLoadingInSeparateClassloaders() throws Exception {
		String root = "file://"+System.getProperty("user.dir") + "/spikes/test-resources/classloader/";
		URL api1 = new URL(root + "brickOne/brickOne-api-1.0.jar");
		URL api2 = new URL(root + "brickTwo/brickTwo-api-1.0.jar");

		URL impl1 = new URL(root + "brickOne/brickOne-impl-1.0.jar");
		URL impl2 = new URL(root + "brickTwo/brickTwo-impl-1.0.jar");

		/* Interface classloader */
		ClassLoader parent = new URLClassLoader(new URL[]{api1, api2});
		
		/* BrickOne classloader */
		ClassLoader cl1 = new URLClassLoader(new URL[]{impl1}, parent);
		assertSame(parent, cl1.getParent());
		
		/* BrickTwo classloader */
		ClassLoader cl2 = new URLClassLoader(new URL[]{impl2}, parent);
		assertSame(parent, cl2.getParent());
		
		// test brick ONE
		Class<?> brickOneImpl = cl1.loadClass("org.sneer.lego.tests.brickOne.impl.BrickOneImpl");
		Object brickOne = brickOneImpl.newInstance();
		assertSame("BrickOneImpl should be loaded from it's own classloader",cl1, brickOneImpl.getClassLoader());
		assertSame("BrickOne should be loaded from the api classloader", parent, brickOneImpl.getInterfaces()[0].getClassLoader());

		Method m1 = brickOneImpl.getMethod("doAnything", (Class<?>[]) null);
		Class<?> returnType = m1.getReturnType();
		assertSame("SomeValue should be loaded from the api classloader", parent, returnType.getClassLoader());

		Object result1 = m1.invoke(brickOne, (Object[])null);
		assertSame("SomeValueImpl should be loaded from it's own classloader",cl1, result1.getClass().getClassLoader());
		
		// test brick TWO
		Class<?> brickTwoImpl = cl2.loadClass("org.sneer.lego.tests.brickTwo.impl.BrickTwoImpl");
		assertSame("BrickTwoImpl should be loaded from it's own classloader",cl2, brickTwoImpl.getClassLoader());
		assertSame("BrickTwo should be loaded from the api classloader", parent, brickTwoImpl.getInterfaces()[0].getClassLoader());
		
		
		//this should be done by our container
		Object brickTwo = brickTwoImpl.newInstance();
		Field field = brickTwo.getClass().getDeclaredField("_one");
		field.setAccessible(true);
		field.set(brickTwo, brickOne);
		
		Method m2 = brickTwoImpl.getMethod("doSomething", (Class<?>[]) null);
		Object result2 = m2.invoke(brickTwo, (Object[])null);
		assertEquals("b1 brick2", result2);
	}	
}
