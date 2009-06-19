package sneer.foundation.brickness.impl.tests;

import org.junit.Assert;
import org.junit.Test;

import sneer.foundation.brickness.BrickLoadingException;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.brickness.impl.tests.fixtures.a.BrickA;
import sneer.foundation.brickness.impl.tests.fixtures.b.BrickB;
import sneer.foundation.brickness.impl.tests.fixtures.noannotation.InterfaceWithoutBrickAnnotation;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.EnvironmentUtils;


public class BricknessTest extends Assert {
	
	Environment subject = Brickness.newBrickContainer();

	protected void loadBrick(final Class<?> brick) {
		EnvironmentUtils.retrieveFrom(subject, brick);
	}

	@Test
	public void runDependentBrick() throws Exception {
		
		loadBrick(BrickA.class);

		System.setProperty("BrickA.property", "");
		loadBrick(BrickB.class);
		assertEquals("BrickB was here!", System.getProperty("BrickA.property"));
	}

	@Test
	public void libsRunInSeparateClassloaders() throws Exception {
		System.setProperty("BrickA.lib.classloader", "");
		System.setProperty("BrickB.lib.classloader", "");
		loadBrick(BrickA.class);
		loadBrick(BrickB.class);
		String classLoaderA = System.getProperty("BrickA.lib.classLoader");
		String classLoaderB = System.getProperty("BrickB.lib.classLoader");

		assertFalse(classLoaderA.equals(classLoaderB));
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
