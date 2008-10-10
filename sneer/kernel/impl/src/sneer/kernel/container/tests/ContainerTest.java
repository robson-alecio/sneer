package sneer.kernel.container.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.Injector;
import sneer.kernel.container.impl.AnnotatedFieldInjector;
import sneer.kernel.container.impl.ContainerImpl;
import sneer.kernel.container.impl.StaticFieldInjector;
import sneer.kernel.container.tests.impl.MySample;

public class ContainerTest {

	@Test
	public void testImplementationBinding() throws Exception {
        Sample sample = new MySample();
        Container c = new ContainerImpl(sample);
        Sample subject = c.produce(Sample.class);
        assertSame(sample, subject);
	}
	
	
	@Test
	public void testInjectInjector() throws Exception {
		Container c = new ContainerImpl();
		UsesInjector component = c.produce(UsesInjector.class);
		Injector injector = component.injector();
		assertNotNull(injector);
		assertTrue(injector instanceof AnnotatedFieldInjector || injector instanceof StaticFieldInjector);
	}
	
	@Test
	public void testInjectStaticField() throws Exception {
		Container c = new ContainerImpl();
		assertNull(Static.sample);
		Static ignored = c.produce(Static.class);
		ignored.toString();
		assertNotNull(Static.sample);
	}
	
}
