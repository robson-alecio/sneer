package sneer.container.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import sneer.container.BrickLoadingException;
import sneer.container.NewContainer;
import sneer.container.NewContainers;
import sneer.container.tests.fixtures.a.BrickA;
import sneer.container.tests.fixtures.b.BrickB;
import sneer.container.tests.fixtures.bogus.MissingAnnotation;

public class NewContainerTest {
	
	final NewContainer subject = NewContainers.newContainer();
	
	@Test
	public void runBrick() throws Exception {
		System.setProperty("BrickA.ran", "false");
		runBrick(BrickA.class);
		assertEquals("true", System.getProperty("BrickA.ran"));
	}
	
	@Test
	public void runDependentBrick() throws Exception {
		runBrick(BrickA.class);
		System.setProperty("BrickA.property", "");
		runBrick(BrickB.class);
		assertEquals("BrickB was here!", System.getProperty("BrickA.property"));
	}
	
	@Test(expected=BrickLoadingException.class)
	public void runDependentBrickWithoutDependencies() throws Exception {
		runBrick(BrickB.class);
	}
	
	@Test(expected=BrickLoadingException.class)
	public void noBrickInterfaceFound() throws Exception {
		runBrick(MissingAnnotation.class);
	}
	
	@Test(expected=FileNotFoundException.class)
	public void bogusDirectory() throws Exception {
		subject.runBrick("bogus");
	}
	
	private void runBrick(final Class<?> brick) throws IOException,
		URISyntaxException {
		subject.runBrick(directoryFor(brick));
	}

	private String directoryFor(Class<?> klass) throws URISyntaxException {
		final String fileName = klass.getCanonicalName().replace('.', '/') + ".class";
		final URL url = klass.getResource("/" + fileName);
		return new File(url.toURI()).getParent();
	}
}
