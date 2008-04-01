package sneer.lego.tests;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import sneer.lego.Binder;
import sneer.lego.Container;
import sneer.lego.Injector;
import sneer.lego.impl.FieldInjector;
import sneer.lego.impl.SimpleBinder;
import sneer.lego.impl.SimpleContainer;
import sneer.lego.tests.impl.MySample;

public class ContainerTest extends BrickTestSupport {

	@Test
	public void testAssignable() {
		assertTrue(Object.class.isAssignableFrom(String.class));
		assertTrue(Object.class.isAssignableFrom(Integer.class));
		assertTrue(Number.class.isAssignableFrom(Integer.class));
		assertTrue(Container.class.isAssignableFrom(SimpleContainer.class));
	}
	
	@Test
	public void testBinder() throws Exception {
		Binder binder = new SimpleBinder();
		binder.bind(Sample.class).to(MySample.class);
		Container c = new SimpleContainer(binder);
		Sample sample = c.produce(Sample.class);
		assertTrue(sample instanceof MySample);
	}
	
	@Test
	public void testBindToInstance() throws Exception {
        Binder binder = new SimpleBinder();
        Sample sample = new Sample() {};

        binder.bind(Sample.class).toInstance(sample);
        Container c = new SimpleContainer(binder);
        Sample subject = c.produce(Sample.class);
        assertSame(sample, subject);
	}
	
	@Test
	public void testLifecycle() throws Exception {
		Container c = new SimpleContainer();
        Lifecycle lifecycle = c.produce(Lifecycle.class);
        assertTrue(lifecycle.configureCalled());
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
		assertTrue(injector instanceof FieldInjector);
	}
}
