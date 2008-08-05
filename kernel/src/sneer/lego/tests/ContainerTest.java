package sneer.lego.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;

import org.junit.Ignore;
import org.junit.Test;

import sneer.lego.Container;
import sneer.lego.Injector;
import sneer.lego.impl.AnnotatedFieldInjector;
import sneer.lego.impl.SimpleContainer;
import sneer.lego.impl.StaticFieldInjector;
import sneer.lego.tests.impl.MySample;

public class ContainerTest {

	@Test
	public void testImplementationBinding() throws Exception {
        Sample sample = new MySample();
        Container c = new SimpleContainer(sample);
        Sample subject = c.produce(Sample.class);
        assertSame(sample, subject);
	}
	
	@Test
	public void testLifecycle() throws Exception {
		Container c = new SimpleContainer();
        Lifecycle lifecycle = c.produce(Lifecycle.class);
        assertTrue(lifecycle.startCalled());
	}
	
	@Test
	public void testCreateWithArgs() throws Exception {
		Container c = new SimpleContainer();
		ConcreteWithParameters concrete = c.create(ConcreteWithParameters.class, "some string");
		assertEquals("some string", concrete.getS());
		assertEquals(0, concrete.getI());

		
		concrete = c.create(ConcreteWithParameters.class, new SomeInterfaceImpl(77));
		assertEquals(77, concrete.getInterface().getValue());

		concrete = c.create(ConcreteWithParameters.class, "x", 99);
		assertEquals("x", concrete.getS());
		assertEquals(99, concrete.getI());
		
		try {
			concrete = c.create(ConcreteWithParameters.class, c);
			fail("should have thrown exception");
		} catch(Exception ignored) {}
	}
	
	@Test
	public void testInjectInjector() throws Exception {
		Container c = new SimpleContainer();
		UsesInjector component = c.produce(UsesInjector.class);
		Injector injector = component.injector();
		assertNotNull(injector);
		assertTrue(injector instanceof AnnotatedFieldInjector || injector instanceof StaticFieldInjector);
	}

	@Test
	@Ignore
	public void testMakeSerializable()  throws Exception {
		Container c = new SimpleContainer();
		MakeMeSerializable component = c.produce(MakeMeSerializable.class);
		assertTrue(component instanceof Serializable);
	}

	@Test
	public void testInjectStaticField() throws Exception {
		Container c = new SimpleContainer();
		assertNull(Static.sample);
		Static ignored = c.produce(Static.class);
		ignored.toString();
		assertNotNull(Static.sample);
	}
	
}
