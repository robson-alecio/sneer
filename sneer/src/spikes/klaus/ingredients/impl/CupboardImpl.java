package spikes.klaus.ingredients.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import spikes.klaus.ingredients.Cupboard;
import spikes.klaus.ingredients.Ingredient;

public class CupboardImpl implements Cupboard {

	private Map<String, Ingredient> _ingredients = new HashMap<String, Ingredient>();

	public Map<String, Ingredient> ingredients() {
		return _ingredients;
	}

	public void storeIngredientJar(File file) throws IOException {
		JarFile ingredientJarFile = new JarFile(file);
		Manifest manifest = ingredientJarFile.getManifest();
		Attributes attributes = manifest.getMainAttributes();
		String ingredientName = attributes.getValue("Main-Class");
		Ingredient ingredient = new IngredientImpl();
		_ingredients.put(ingredientName, ingredient);
	}

}
