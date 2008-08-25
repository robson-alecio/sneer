package sneer.skin.widgets.reactive;

import java.util.Iterator;


public interface ListModel extends javax.swing.ListModel {

	int indexOf(Object element);
	Iterator<Object> elements();
	void addElement(Object element);
	void addElementAt(Object element, int index);
	void removeElement(Object element);
	void removeElementAt(int fromIndex);
}