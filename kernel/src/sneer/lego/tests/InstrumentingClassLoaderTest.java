package sneer.lego.tests;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Test;

import sneer.lego.impl.classloader.Enhancer;
import sneer.lego.impl.classloader.EnhancingClassLoader;
import sneer.lego.impl.classloader.MakeSerializable;

public class InstrumentingClassLoaderTest {

	@Test
	public void testVisitClass() throws Exception {
		Enhancer enhancer = new MakeSerializable();
		//Enhancer enhancer = new NoEnhancement();
		ClassLoader cl = new EnhancingClassLoader(enhancer);
		Class<?> subject = cl.loadClass("sneer.lego.tests.SimpleClass");
		Object instance = subject.newInstance();
		assertTrue(instance instanceof Serializable);
	}
}
