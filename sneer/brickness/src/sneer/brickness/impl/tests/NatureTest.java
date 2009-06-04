package sneer.brickness.impl.tests;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

import sneer.brickness.BrickLoadingException;
import sneer.brickness.Brickness;
import sneer.brickness.ClassDefinition;
import sneer.brickness.impl.tests.fixtures.nature.brick.BrickOfSomeNature;
import sneer.brickness.impl.tests.fixtures.nature.brick.impl.BrickOfSomeNatureImpl;
import sneer.brickness.impl.tests.fixtures.nature.provider.SomeNature;
import sneer.brickness.testsupport.ClassFiles;
import sneer.commons.environments.Environment;
import sneer.commons.environments.EnvironmentUtils;

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

	private byte[] bytecodeFor(final Class<?> clazz)
			throws IOException {
		return FileUtils.readFileToByteArray(ClassFiles.fileFor(clazz));
	}
}
