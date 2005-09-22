package sneer.ui.topten;

import java.util.ArrayList;
import java.util.List;

import sneer.Sneer;
import sneer.life.LifeView;

public class TopTen {

	private final Sneer _sneer;

	public TopTen(Sneer sneer) {	_sneer = sneer; }

	public void newCategory(String newCategory) {
		List<String> list = categories();
		if (list == null) {
			initTopTen();
			newCategory(newCategory);
			return;
		}
		list.add(newCategory);
	}

	private void initTopTen() {
		_sneer.life().thing("TopTenCategoryList", new ArrayList<String>());
	}

	private List<String> categories() {
		return categoriesList(_sneer.life());
	}

	public @SuppressWarnings("unchecked") ArrayList<String> categoriesList(LifeView lifeView) {
		return (ArrayList<String>)lifeView.thing("TopTenCategoryList");
	}

}
