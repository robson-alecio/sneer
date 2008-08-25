package sneer.skin.widgets.reactive;

import java.awt.Component;
import java.util.Enumeration;

import javax.swing.JComponent;

public interface ListWidget<ELEMENT> {

	Enumeration<ELEMENT> elements();

	void addElement(ELEMENT element);
	void removeElement(ELEMENT element);

	void insertElementAt(ELEMENT element, int index);
	void removeElementAt(int index);
	ELEMENT getElementAt(int index);
	
	int indexOf(ELEMENT element);
	int listSize();	
	
	Component getComponent();
	JComponent getMainWidget();


}
