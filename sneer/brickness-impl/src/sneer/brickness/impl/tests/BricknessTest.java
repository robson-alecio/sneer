package sneer.brickness.impl.tests;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import sneer.brickness.impl.BrickPlacementException;
import sneer.brickness.impl.Brickness;
import sneer.brickness.impl.tests.fixtures.a.BrickA;
import sneer.brickness.impl.tests.fixtures.b.BrickB;
import sneer.brickness.impl.tests.fixtures.noannotation.InterfaceWithoutBrickAnnotation;
import wheel.io.Jars;

public class BricknessTest extends Assert {
	
	Brickness subject = new Brickness();

	protected void placeBrick(final Class<?> brick) {
		subject.placeBrick(Jars.classpathRootFor(brick), brick.getName());
	}

	@Test
	public void runDependentBrick() throws Exception {
		
		placeBrick(BrickA.class);

		System.setProperty("BrickA.property", "");
		placeBrick(BrickB.class);
		assertEquals("BrickB was here!", System.getProperty("BrickA.property"));
	}

	@Test
	public void runInSeparateClassloaders() throws Exception {
		
		System.setProperty("BrickA.classloader", "");
		System.setProperty("BrickB.classloader", "");
		placeBrick(BrickA.class);
		placeBrick(BrickB.class);
		String classLoaderA = System.getProperty("BrickA.classLoader");
		String classLoaderB = System.getProperty("BrickB.classLoader");

		assertFalse(classLoaderA.equals(classLoaderB));
	}
	
	@Test
	public void runBrick() throws Exception {
		System.setProperty("BrickA.ran", "false");
		placeBrick(BrickA.class);
		assertEquals("true", System.getProperty("BrickA.ran"));
	}
	
	@Test(expected=BrickPlacementException.class)
	public void runDependentBrickWithoutDependencies() throws Exception {
		placeBrick(BrickB.class);
	}
	
	@Test(expected=BrickPlacementException.class)
	public void noBrickInterfaceFound() throws Exception {
		placeBrick(InterfaceWithoutBrickAnnotation.class);
	}

	@Test(expected=BrickPlacementException.class)
	public void bogusDirectory() throws Exception {
		subject.placeBrick(new File("bogus"), "bogus");
	}
}
