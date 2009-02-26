package sneer.kernel.container.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.Containers;
import sneer.kernel.container.SneerConfig;
import sneer.pulp.config.persistence.PersistenceConfig;
import wheel.lang.Environment;

public class ContainerTest {
	
	@Test
	public void testEnvironmentIntegration() throws Exception {
		final Mockery mockery = new JUnit4Mockery();
		final Environment environment = mockery.mock(Environment.class);
		mockery.checking(new Expectations() {{
			one(environment).provide(SneerConfig.class); will(returnValue(null));
			one(environment).provide(PersistenceConfig.class); will(returnValue(null));
			one(environment).provide(Object.class); will(returnValue("o"));
			one(environment).provide(Sample.class); will(returnValue(null));
		}});
		
		final Container container = Containers.newContainer(environment);
		
		assertEquals("o", container.provide(Object.class));
		assertEquals("SampleImpl", container.provide(Sample.class).getClass().getSimpleName());
		
		mockery.assertIsSatisfied();
	}

	@Test
	public void testImplementationBinding() throws Exception {
        Sample sample = new Sample() {};
        Container c = Containers.newContainer(sample);
        Sample subject = c.provide(Sample.class);
        assertSame(sample, subject);
	}
}
