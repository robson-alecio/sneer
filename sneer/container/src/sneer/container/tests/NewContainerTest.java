package sneer.container.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import sneer.container.NewContainer;
import sneer.container.NewContainers;
import sneer.container.tests.fixtures.TestBrick;

public class NewContainerTest {
	
	final NewContainer subject = NewContainers.newContainer();
	
	@Test
	public void runBrick() throws Exception {
		
		System.setProperty("TestBrick.ran", "false");
		subject.runBrick(directoryFor(TestBrick.class));
		assertEquals("true", System.getProperty("TestBrick.ran"));
		
	}

	private String directoryFor(Class<?> klass) throws URISyntaxException {
		final String fileName = klass.getCanonicalName().replace('.', '/') + ".class";
		final URL url = klass.getResource("/" + fileName);
		return new File(url.toURI()).getParent();
	}
}
