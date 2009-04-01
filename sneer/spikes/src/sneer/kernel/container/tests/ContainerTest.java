package sneer.kernel.container.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.commons.environments.Environment;
import sneer.kernel.container.ContainerOld;
import sneer.kernel.container.ContainersOld;
import sneer.kernel.container.SneerConfig;

public class ContainerTest {
	
	@Test
	public void testEnvironmentIntegration() throws Exception {
		final Mockery mockery = new JUnit4Mockery();
		final Environment environment = mockery.mock(Environment.class);
		mockery.checking(new Expectations() {{
			one(environment).provide(SneerConfig.class); will(returnValue(null));
			one(environment).provide(Object.class); will(returnValue("o"));
			one(environment).provide(Sample.class); will(returnValue(null));
		}});
		
		final ContainerOld container = ContainersOld.newContainer(environment);
		
		assertEquals("o", container.provide(Object.class));
		assertEquals("SampleImpl", container.provide(Sample.class).getClass().getSimpleName());
		
		mockery.assertIsSatisfied();
	}

	@Test
	public void testImplementationBinding() throws Exception {
        Sample sample = new Sample() {};
        ContainerOld c = ContainersOld.newContainer(sample);
        Sample subject = c.provide(Sample.class);
        assertSame(sample, subject);
	}
}
