package sneer.skin.widgets.reactive;

import javax.swing.JComponent;

import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface TextWidget<WIDGET extends JComponent> extends Widget<WIDGET> {

	Signal<String> output();
	
	Consumer<String> setter();	

	JComponent[] getWidgets();
}