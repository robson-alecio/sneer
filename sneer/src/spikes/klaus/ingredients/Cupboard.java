package spikes.klaus.ingredients;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface Cupboard {

	Map<String, Ingredient> ingredients();

	void storeIngredientJar(File file) throws IOException;

}
