package sneer.skin.widgets.reactive;

import javax.swing.JComponent;

import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.pulp.reactive.Signal;


public interface TextWidget<WIDGET extends JComponent> extends ComponentWidget<WIDGET> {

	Signal<?> output();
	
	PickyConsumer<? super String> setter();	

	JComponent[] getWidgets();
}