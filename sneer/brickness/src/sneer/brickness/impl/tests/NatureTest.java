package sneer.brickness.impl.tests;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

import sneer.brickness.Brickness;
import sneer.brickness.BricknessFactory;
import sneer.brickness.ClassDefinition;
import sneer.brickness.impl.BrickPlacementException;
import sneer.brickness.impl.IllegalNatureException;
import sneer.brickness.impl.tests.fixtures.nature.brick.BrickOfSomeNature;
import sneer.brickness.impl.tests.fixtures.nature.brick.impl.BrickOfSomeNatureImpl;
import sneer.brickness.impl.tests.fixtures.nature.provider.SomeNature;
import wheel.io.Jars;

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
		
		placeBrick(BricknessFactory.newBrickContainer(nature), BrickOfSomeNature.class);
		
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void natureWithoutImplementation() {
		
		try {
			placeBrick(BricknessFactory.newBrickContainer(), BrickOfSomeNature.class);
			Assert.fail();
		} catch (BrickPlacementException e) {
			assertTrue(e.getCause() instanceof IllegalNatureException);
		}
	}
	
	protected void placeBrick(Brickness subject, final Class<?> brick) {
		subject.placeBrick(Jars.classpathRootFor(brick), brick.getName());
	}

	private byte[] bytecodeFor(final Class<?> clazz)
			throws IOException {
		return FileUtils.readFileToByteArray(Jars.fileFor(clazz));
	}
}
