package sneer.brickness.impl.tests;

import org.junit.Assert;
import org.junit.Test;

import sneer.brickness.BrickLoadingException;
import sneer.brickness.Brickness;
import sneer.brickness.BricknessFactory;
import sneer.brickness.impl.tests.fixtures.a.BrickA;
import sneer.brickness.impl.tests.fixtures.b.BrickB;
import sneer.brickness.impl.tests.fixtures.noannotation.InterfaceWithoutBrickAnnotation;

public class BricknessTest extends Assert {
	
	Brickness subject = BricknessFactory.newBrickContainer();

	protected void loadBrick(final Class<?> brick) {
		subject.environment().provide(brick);
	}

	@Test
	public void runDependentBrick() throws Exception {
		
		loadBrick(BrickA.class);

		System.setProperty("BrickA.property", "");
		loadBrick(BrickB.class);
		assertEquals("BrickB was here!", System.getProperty("BrickA.property"));
	}

	@Test
	public void runInSeparateClassloaders() throws Exception {
		
		System.setProperty("BrickA.classloader", "");
		System.setProperty("BrickB.classloader", "");
		loadBrick(BrickA.class);
		loadBrick(BrickB.class);
		String classLoaderA = System.getProperty("BrickA.classLoader");
		String classLoaderB = System.getProperty("BrickB.classLoader");

		assertFalse(classLoaderA.equals(classLoaderB));
	}
	
	@Test
	public void runBrick() throws Exception {
		System.setProperty("BrickA.ran", "false");
		loadBrick(BrickA.class);
		assertEquals("true", System.getProperty("BrickA.ran"));
	}
	
	@Test
	public void runDependentBrickWithoutDependencies() throws Exception {
		loadBrick(BrickB.class);
	}
	
	@Test(expected=BrickLoadingException.class)
	public void noBrickInterfaceFound() throws Exception {
		loadBrick(InterfaceWithoutBrickAnnotation.class);
	}

}
