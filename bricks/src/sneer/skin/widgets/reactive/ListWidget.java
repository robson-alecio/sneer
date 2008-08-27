package sneer.skin.widgets.reactive;

import java.awt.Component;

import javax.swing.JComponent;

import wheel.reactive.lists.ListRegister;

public interface ListWidget<ELEMENT> {

	ListRegister<ELEMENT> register();

	Component getComponent();
	JComponent getMainWidget();
	
}
