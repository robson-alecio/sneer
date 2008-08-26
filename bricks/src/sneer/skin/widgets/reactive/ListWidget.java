package sneer.skin.widgets.reactive;

import java.awt.Component;
import java.util.Enumeration;

import javax.swing.JComponent;

public interface ListWidget<ELEMENT> {

	Enumeration<ELEMENT> elements();

	void addElement(ELEMENT element);
	void addElementAt(ELEMENT element, int index);

	void removeElement(ELEMENT element);
	void removeElementAt(int index);

	ELEMENT get(int index);
	
	int indexOf(ELEMENT element);
	int listSize();	
	
	Component getComponent();
	JComponent getMainWidget();
	
}
