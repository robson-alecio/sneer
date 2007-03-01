package spikes.klaus.ingredients.tests;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import spikes.klaus.ingredients.Cupboard;

abstract public class IngredientsTest extends TestCase {

	private Cupboard _cupboard = subject();

	public void testIngredientStorage() throws IOException {
		assertTrue(_cupboard.ingredients().isEmpty());

		String jarFile = IngredientsTest.class.getResource("ingredient1.jar").getFile();
		_cupboard.storeIngredientJar(new File(jarFile));
		
		assertTrue(_cupboard.ingredients().containsKey("Ingredient1"));
		
	}

	protected abstract Cupboard subject();
	
}
