package sneer.kernel.container.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Ignore;
import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.Injector;
import sneer.kernel.container.impl.AnnotatedFieldInjector;
import sneer.kernel.container.impl.SimpleContainer;
import sneer.kernel.container.impl.StaticFieldInjector;
import sneer.kernel.container.tests.impl.MySample;

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
		assertTrue(instantiateBrick() instanceof Serializable);
	}

	@Test
	public void testIsNotSerializable()  throws Exception {
		assertTrue( !(instantiateBrick() instanceof Serializable) );
	}

	private MakeMeSerializable instantiateBrick() {
		Container c = new SimpleContainer();
		MakeMeSerializable component = c.produce(MakeMeSerializable.class);
		assertNotNull(component);
		return component;
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
