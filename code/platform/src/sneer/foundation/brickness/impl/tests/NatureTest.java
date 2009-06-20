package sneer.foundation.brickness.impl.tests;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

import sneer.foundation.brickness.BrickLoadingException;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.brickness.ClassDefinition;
import sneer.foundation.brickness.impl.tests.fixtures.nature.brick.BrickOfSomeNature;
import sneer.foundation.brickness.impl.tests.fixtures.nature.brick.impl.BrickOfSomeNatureImpl;
import sneer.foundation.brickness.impl.tests.fixtures.nature.provider.SomeNature;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.EnvironmentUtils;

// TODO: test multiple natures
public class NatureTest extends Assert {
	
	@Test
	public void realizeIsCalled() throws Exception {
		final Mockery mockery = new JUnit4Mockery();
		final SomeNature nature = mockery.mock(SomeNature.class);
		
		mockery.checking(new Expectations() {{
			
			final Class<BrickOfSomeNatureImpl> clazz = BrickOfSomeNatureImpl.class;
			final byte[] brickImplBytecode = bytecodeFor(clazz);
			final ClassDefinition classDef = new ClassDefinition(clazz.getName(), brickImplBytecode);
			exactly(1).of(nature).realize(classDef);
				will(returnValue(Arrays.asList(classDef)));
		}});
		
		loadBrick(Brickness.newBrickContainer(nature), BrickOfSomeNature.class);
		
		mockery.assertIsSatisfied();
	}
	
	@Test (expected = BrickLoadingException.class)
	public void natureWithoutImplementation() {
		final Class<BrickOfSomeNature> brick = BrickOfSomeNature.class;
		Environment container = Brickness.newBrickContainer();
		loadBrick(container, brick);
	}

	private void loadBrick(Environment container, final Class<?> brick) {
		EnvironmentUtils.retrieveFrom(container, brick);
	}

	private byte[] bytecodeFor(final Class<?> clazz) throws IOException {
		InputStream in = null;
		try {
			in = new FileInputStream(fileFor(clazz));
		    ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024*4];
			int n = 0;
			while (-1 != (n = in.read(buffer))) 
				output.write(buffer, 0, n);
			
			return output.toByteArray();
		} finally {
		    try { in.close(); } catch (Exception ignore) {}
		}
	}

	private File fileFor(final Class<?> clazz) {
		final String fileName = clazz.getCanonicalName().replace('.', '/') + ".class";
		return new File(toURI(clazz.getResource("/" + fileName)));
	}
	
	private static URI toURI(final URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new IllegalStateException();
		}
	}

}
