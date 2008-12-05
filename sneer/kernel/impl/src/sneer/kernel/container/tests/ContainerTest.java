package sneer.kernel.container.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Injector;
import sneer.kernel.container.SneerConfig;
import sneer.kernel.container.impl.AnnotatedFieldInjector;
import sneer.kernel.container.impl.ContainerImpl;
import sneer.kernel.container.impl.StaticFieldInjector;
import sneer.kernel.container.tests.impl.MySample;
import sneer.kernel.container.tests.impl.SampleImpl;
import sneer.pulp.config.persistence.PersistenceConfig;
import wheel.lang.Environment;

public class ContainerTest {
	
	@Test
	public void testEnvironmentIntegration() throws Exception {
		final Mockery mockery = new JUnit4Mockery();
		final Environment environment = mockery.mock(Environment.class);
		mockery.checking(new Expectations() {{
			one(environment).provide(SneerConfig.class);
				will(returnValue(null));
			one(environment).provide(PersistenceConfig.class);
				will(returnValue(null));
			atLeast(1).of(environment).provide(Injector.class);
				will(returnValue(null));
			one(environment).provide(Object.class);
				will(returnValue("o"));
			one(environment).provide(Sample.class);
				will(returnValue(null));
		}});
		
		final Container container = ContainerUtils.newContainer(environment);
		
		assertEquals("o", container.provide(Object.class));
		assertTrue(container.provide(Sample.class) instanceof SampleImpl);
		
		mockery.assertIsSatisfied();
	}

	@Test
	public void testImplementationBinding() throws Exception {
        Sample sample = new MySample();
        Container c = new ContainerImpl(sample);
        Sample subject = c.provide(Sample.class);
        assertSame(sample, subject);
	}
	
	
	@Test
	public void testInjectInjector() throws Exception {
		Container c = new ContainerImpl();
		UsesInjector component = c.provide(UsesInjector.class);
		Injector injector = component.injector();
		assertNotNull(injector);
		assertTrue(injector instanceof AnnotatedFieldInjector || injector instanceof StaticFieldInjector);
	}
	
	@Test
	public void testInjectStaticField() throws Exception {
		Container c = new ContainerImpl();
		assertNull(Static.sample);
		Static ignored = c.provide(Static.class);
		ignored.toString();
		assertNotNull(Static.sample);
	}
	
}
