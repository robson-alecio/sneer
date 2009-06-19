package sneer.bricks.skin.widgets.reactive;

import javax.swing.JComponent;

public interface ComponentWidget<MAINCOMPONENT extends JComponent> extends Widget<MAINCOMPONENT>{

	JComponent getComponent();

}