package sneer.skin.widgets.reactive;

import javax.swing.JComponent;

public interface Widget<MAINCOMPONENT extends JComponent> {

	JComponent getComponent();
	MAINCOMPONENT getMainWidget();

}
