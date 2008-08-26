package sneer.skin.widgets.reactive;

import java.util.Iterator;


public interface ListModel<ELEMENT> extends javax.swing.ListModel {

	int indexOf(ELEMENT element);
	Iterator<ELEMENT> elements();
	void addElement(ELEMENT element);
	void addElementAt(ELEMENT element, int index);
	void removeElement(ELEMENT element);
	void removeElementAt(int fromIndex);
}