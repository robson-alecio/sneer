package sneer.skin.widgets.reactive;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import wheel.reactive.Signal;

public abstract class LabelProvider<ELEMENT> {

	protected final Map<ELEMENT, Signal<Image>> _cacheImages = new HashMap<ELEMENT, Signal<Image>>();

	public Signal<Image> cachedImageFor(ELEMENT element) {
		if (!_cacheImages.containsKey(element))
			_cacheImages.put(element, imageFor(element));
		
		return _cacheImages.get(element);
	}

	public abstract Signal<Image> imageFor(ELEMENT element);
	public abstract Signal<String> labelFor(ELEMENT element);

}
