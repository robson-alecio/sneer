package sneer.skin.widgets.reactive;

import java.awt.Container;

public interface Widget<MAINCOMPONENT extends Container> {

	MAINCOMPONENT getMainWidget();

}
