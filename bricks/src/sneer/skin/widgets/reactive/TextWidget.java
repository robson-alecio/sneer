package sneer.skin.widgets.reactive;

import java.awt.Container;

import javax.swing.JComponent;

public interface TextWidget {

	String getText();
	
	void setText(String text);
	
	JComponent getMainWidget();
	
	JComponent[] getWidgets();

	Container getContainer();

}