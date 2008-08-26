package sneer.skin.widgets.reactive.impl;

import static wheel.lang.Types.cast;

import java.awt.Component;
import java.util.Enumeration;

import javax.swing.JList;

import sneer.skin.widgets.reactive.ListModel;
import sneer.skin.widgets.reactive.ListWidget;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public class RListImpl<ELEMENT> extends JList implements ListWidget<ELEMENT> {
	
	private static final long serialVersionUID = 1L;

	protected Signal<Object[]> _source;
	protected Consumer<Object[]> _setter;

	
	RListImpl(Signal<Object[]> source, Consumer<Object[]> setter) {
		_source = source;
		_setter = setter;

		initModel();
		addDnDListeners();	
		addReceiverListener();
	}

	private void addReceiverListener() {
		//Implement:  addListReceiver(new Omnivore<ListValueChange>() 
//		_source.addListReceiver(new Omnivore<ListValueChange>(){
//			@Override
//			public void consume(ListValueChange valueObject) {
//				revalidate();
//				repaint();
//		}});
	}

	private void addDnDListeners() {
		ListAnimatorDecorator smoother = new ListAnimatorDecorator(this) {
			@Override
			protected void move(int fromIndex, int toIndex) {
				ListModel model = (ListModel) getModel();
				Object tmp = model.getElementAt(fromIndex);
				model.removeElementAt(fromIndex);
				model.addElementAt(tmp, toIndex);
				revalidate();
				repaint();
			}
		};
		
		ListAnimatorMouseListener listener = new ListAnimatorMouseListener(smoother);
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}
	
	private void initModel() {
		setModel(new ListModelImpl(_source, _setter));
	}

	//Facade to Model
	@Override public void addElement(ELEMENT element) { ((ListModel)cast(getModel())).addElement(element);}
	@Override public Enumeration<ELEMENT> elements() { return cast(((ListModel)cast(getModel())).elements());}
	@Override public ELEMENT getElementAt(int index) { return cast(getModel().getElementAt(index));}
	@Override public int indexOf(ELEMENT element) { return ((ListModel)cast(getModel())).indexOf(element); }
	@Override public void insertElementAt(ELEMENT element, int index) {((ListModel)cast(getModel())).addElementAt(element, index);}
	@Override public void removeElement(ELEMENT element) {((ListModel)cast(getModel())).addElement(element);}
	@Override public void removeElementAt(int index) {((ListModel)cast(getModel())).getElementAt(index);}
	@Override public int listSize(){return this.getModel().getSize();}
	
	@Override
	public JList getMainWidget() {
		return this;
	}
	
	@Override
	public Component getComponent(){
		return this;
	}
}