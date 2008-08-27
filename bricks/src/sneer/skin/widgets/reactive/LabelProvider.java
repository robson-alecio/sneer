package sneer.skin.widgets.reactive;

import java.awt.Image;

import wheel.reactive.Signal;

public interface LabelProvider<ELEMENT> {

	Signal<String> labelFor(ELEMENT element);

	Signal<Image> imageFor(ELEMENT element);

}
