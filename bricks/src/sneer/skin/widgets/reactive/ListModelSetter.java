package sneer.skin.widgets.reactive;

public interface ListModelSetter<ELEMENT> {

	public void addElement(ELEMENT element);
	public void addElementAt(ELEMENT element, int index);

	public void removeElementAt(int index);
	public void removeElement(ELEMENT element);
}
