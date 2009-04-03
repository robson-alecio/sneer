package sneer.skin.widgets.reactive;

import javax.swing.JComponent;

import sneer.pulp.reactive.Signal;

import wheel.lang.PickyConsumer;

public interface TextWidget<WIDGET extends JComponent> extends ComponentWidget<WIDGET> {

	Signal<?> output();
	
	PickyConsumer<?> setter();	

	JComponent[] getWidgets();
}