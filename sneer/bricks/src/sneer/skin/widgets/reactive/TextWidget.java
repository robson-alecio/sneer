package sneer.skin.widgets.reactive;

import javax.swing.JComponent;

import sneer.pulp.reactive.Signal;

import wheel.lang.PickyConsumer;

public interface TextWidget<WIDGET extends JComponent> extends ComponentWidget<WIDGET> {

	Signal<String> output();
	
	PickyConsumer<String> setter();	

	JComponent[] getWidgets();
}