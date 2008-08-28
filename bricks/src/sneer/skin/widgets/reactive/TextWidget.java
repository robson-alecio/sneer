package sneer.skin.widgets.reactive;

import javax.swing.JComponent;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface TextWidget<WIDGET extends JComponent> extends Widget<WIDGET> {

	Signal<String> output();
	Omnivore<String> setter();	

	JComponent[] getWidgets();
}