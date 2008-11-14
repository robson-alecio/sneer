package sneer.skin.widgets.reactive;

import javax.swing.JComponent;

import wheel.lang.PickyConsumer;
import wheel.reactive.Signal;

public interface TextWidget<WIDGET extends JComponent> extends ComponentWidget<WIDGET> {

	Signal<String> output();
	
	PickyConsumer<String> setter();	

	JComponent[] getWidgets();
}