package sneer.skin.widgets.reactive;

import java.awt.Container;

import javax.swing.JComponent;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface TextWidget<WIDGET extends JComponent> {

	Signal<String> output();
	Omnivore<String> setter();	

	WIDGET getMainWidget();
	JComponent[] getWidgets();
	Container getComponent();
}