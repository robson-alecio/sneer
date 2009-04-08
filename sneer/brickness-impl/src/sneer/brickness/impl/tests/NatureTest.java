package sneer.brickness.impl.tests;

import org.apache.commons.io.FileUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

import sneer.brickness.impl.BrickPlacementException;
import sneer.brickness.impl.Brickness;
import sneer.brickness.impl.IllegalNatureException;
import sneer.brickness.impl.tests.fixtures.nature.brick.BrickOfSomeNature;
import sneer.brickness.impl.tests.fixtures.nature.brick.impl.BrickOfSomeNatureImpl;
import sneer.brickness.impl.tests.fixtures.nature.provider.SomeNature;
import wheel.io.Jars;

public class NatureTest extends Assert {
	
	@Test
	public void realizeIsCalled() throws Exception {
		final Mockery mockery = new JUnit4Mockery();
		final SomeNature nature = mockery.mock(SomeNature.class);
		
		mockery.checking(new Expectations() {{
			
			final byte[] brickImplBytecode = FileUtils.readFileToByteArray(Jars.fileFor(BrickOfSomeNatureImpl.class));
			exactly(1).of(nature).realize(brickImplBytecode);
				will(returnValue(brickImplBytecode));
			
		}});
		
		placeBrick(new Brickness(nature), BrickOfSomeNature.class);
		
		mockery.assertIsSatisfied();
	}
	
	
	@Test
	public void natureWithoutImplementation() {
		
		try {
			placeBrick(new Brickness(), BrickOfSomeNature.class);
			Assert.fail();
		} catch (BrickPlacementException e) {
			assertTrue(e.getCause() instanceof IllegalNatureException);
		}
	}
	
	protected void placeBrick(Brickness subject, final Class<?> brick) {
		subject.placeBrick(Jars.classpathRootFor(brick), brick.getName());
	}
	
}
