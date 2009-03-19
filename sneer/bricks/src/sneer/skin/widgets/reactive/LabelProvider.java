package sneer.skin.widgets.reactive;

import java.awt.Image;

import sneer.pulp.reactive.Signal;


public interface LabelProvider<ELEMENT> {

	Signal<Image> imageFor(ELEMENT element);
	Signal<String> labelFor(ELEMENT element);

}
