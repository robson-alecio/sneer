package sneer.skin.widgets.reactive;

import java.awt.Image;

import wheel.reactive.Signal;

public interface LabelProvider {

	Signal<String> labelFor(Object element);

	Signal<Image> imageFor(Object element);

}
