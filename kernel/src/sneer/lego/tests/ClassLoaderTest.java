package sneer.lego.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

public class ClassLoaderTest {

	@Test
	public void testBrickLoadingInSeparateClassloaders() throws Exception {
		String root = "file://"+System.getProperty("user.dir") + "/kernel/test-resources/classloader/";
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
		Class<?> brickOne = cl1.loadClass("org.sneer.lego.tests.brickOne.impl.BrickOneImpl");
		Object b1 = brickOne.newInstance();
		assertSame("BrickOne implementation should be loaded from cl1",cl1, brickOne.getClassLoader());
		assertSame("BrickOne instances should be loaded from cl1",cl1, b1.getClass().getClassLoader());
		assertSame("BrickOne interface should be loaded from parent", parent, brickOne.getInterfaces()[0].getClassLoader());

		Method m1 = brickOne.getMethod("doAnything", (Class<?>[]) null);
		Class<?> returnType = m1.getReturnType();
		assertSame("SomeValue should be loaded from parent", parent, returnType.getClassLoader());

		Object result1 = m1.invoke(b1, (Object[])null);
		assertSame("SomeValueImpl should be loaded from cl1",cl1, result1.getClass().getClassLoader());
		
		// test brick TWO
		Class<?> brickTwo = cl2.loadClass("org.sneer.lego.tests.brickTwo.impl.BrickTwoImpl");
		Object b2 = brickTwo.newInstance();
		assertSame("BrickTwo implementation should be loaded from cl2",cl2, brickTwo.getClassLoader());
		assertSame("BrickTwo instances should be loaded from cl2",cl2, b2.getClass().getClassLoader());
		assertSame("BrickTwo interface should be loaded from parent", parent, brickTwo.getInterfaces()[0].getClassLoader());
		
		
		//this should be done by our container
		Field field = b2.getClass().getDeclaredField("_one");
		field.setAccessible(true);
		field.set(b2, b1);

		
		Method m2 = brickTwo.getMethod("doSomething", (Class<?>[]) null);
		Object result2 = m2.invoke(b2, (Object[])null);
		assertEquals("b1 brick2", result2);
	}	
	
}
