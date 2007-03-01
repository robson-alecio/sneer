package spikes.klaus.ingredients.impl.tests;

import spikes.klaus.ingredients.Cupboard;
import spikes.klaus.ingredients.impl.CupboardImpl;
import spikes.klaus.ingredients.tests.IngredientsTest;

public class IngredientsImplTest extends IngredientsTest {

	@Override
	protected Cupboard subject() {
		return new CupboardImpl();
	}

}
