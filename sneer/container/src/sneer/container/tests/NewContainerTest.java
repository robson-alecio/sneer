package sneer.container.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import sneer.container.BrickLoadingException;
import sneer.container.NewContainer;
import sneer.container.NewContainers;
import sneer.container.tests.fixtures.a.BrickA;
import sneer.container.tests.fixtures.b.BrickB;
import sneer.container.tests.fixtures.noannotation.InterfaceWithoutBrickAnnotation;

public class NewContainerTest extends Assert {
	
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

	@Test
	public void runInSeparateClassloaders() throws Exception {
		System.setProperty("BrickA.classloader", "");
		System.setProperty("BrickB.classloader", "");
		runBrick(BrickA.class);
		runBrick(BrickB.class);
		String classLoaderA = System.getProperty("BrickA.classLoader");
		String classLoaderB = System.getProperty("BrickB.classLoader");

		assertFalse(classLoaderA.equals(classLoaderB));
	}
	
	@Test(expected=BrickLoadingException.class)
	public void runDependentBrickWithoutDependencies() throws Exception {
		runBrick(BrickB.class);
	}
	
	@Test(expected=BrickLoadingException.class)
	public void noBrickInterfaceFound() throws Exception {
		runBrick(InterfaceWithoutBrickAnnotation.class);
	}
	
	@Test(expected=FileNotFoundException.class)
	public void bogusDirectory() throws Exception {
		subject.runBrick("bogus");
	}
	
	private void runBrick(final Class<?> brick) throws IOException {
		String directory = directoryFor(brick);
		subject.runBrick(directory);
	}

	private String directoryFor(Class<?> klass) {
		final String fileName = klass.getCanonicalName().replace('.', '/') + ".class";
		final URL url = klass.getResource("/" + fileName);
		return new File(toURI(url)).getParent();
	}

	private URI toURI(final URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new IllegalStateException();
		}
	}
}
