package sneer.skin.widgets.reactive.impl;

import static wheel.lang.Types.cast;

import java.awt.Component;
import java.util.Enumeration;

import javax.swing.JList;

import sneer.skin.widgets.reactive.ListModel;
import sneer.skin.widgets.reactive.ListModelSetter;
import sneer.skin.widgets.reactive.ListWidget;
import wheel.reactive.lists.ListSignal;

public class RListImpl<ELEMENT> extends JList implements ListWidget<ELEMENT> {
	
	private static final long serialVersionUID = 1L;

	protected ListSignal<ELEMENT> _source;

	private final ListModelSetter<ELEMENT> _setter;
	
	RListImpl(ListSignal<ELEMENT> source, ListModelSetter<ELEMENT> setter) {
		_source = source;
		_setter = setter;

		initModel();
		addReceiverListener();
	}

	private void addReceiverListener() {
		//Implement this support
//		_source.addListReceiver(new Omnivore<ListValueChange>(){
//			@Override
//			public void consume(ListValueChange valueObject) {
//				revalidate();
//				repaint();
//		}});
	}
	
	private void initModel() {
		setModel(new ListModelImpl<ELEMENT>(_source, _setter));
	}

	//Facade to Model
	@Override
	public void addElement(ELEMENT element) {
		ListModel<ELEMENT> model = cast(getModel());
		model.addElement(element);
	}
	@Override
	public Enumeration<ELEMENT> elements() {
		ListModel<ELEMENT> model = cast(getModel());
		return cast(model.elements());
	}
	@Override
	public ELEMENT get(int index) {
		return cast(getModel().getElementAt(index));
	}
	@Override
	public int indexOf(ELEMENT element) {
		ListModel<ELEMENT> model = cast(getModel());
		return model.indexOf(element);
	}
	@Override
	public void addElementAt(ELEMENT element, int index) {
		ListModel<ELEMENT> model = cast(getModel());
		model.addElementAt(element, index);
	}
	@Override
	public void removeElement(ELEMENT element) {
		ListModel<ELEMENT> model = cast(getModel());
		model.removeElement(element);
	}
	@Override
	public void removeElementAt(int index) {
		ListModel<ELEMENT> model = cast(getModel());
		model.removeElementAt(index);
	}

	@Override
	public int listSize() {
		return this.getModel().getSize();
	}
	
	@Override
	public JList getMainWidget() {
		return this;
	}
	
	@Override
	public Component getComponent(){
		return this;
	}
}