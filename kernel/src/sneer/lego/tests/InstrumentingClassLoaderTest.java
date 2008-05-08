package sneer.lego.tests;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Test;

import sneer.lego.impl.classloader.enhancer.Enhancer;
import sneer.lego.impl.classloader.enhancer.MakeSerializable;

public class InstrumentingClassLoaderTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testVisitClass() throws Exception {
		Enhancer enhancer = new MakeSerializable();
		//Enhancer enhancer = new NoEnhancement();
		ClassLoader cl = new sneer.lego.impl.classloader.InstrumentingClassLoader(enhancer);
		Class<?> subject = cl.loadClass("sneer.lego.tests.SimpleClass");
		Object instance = subject.newInstance();
		assertTrue(instance instanceof Serializable);
	}
}
