package sneer.skin.widgets.reactive.impl;

import static wheel.lang.Types.cast;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.util.Enumeration;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListModel;
import sneer.skin.widgets.reactive.ListModelSetter;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.RFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Signals;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;

public class RListImpl<ELEMENT> extends JList implements ListWidget<ELEMENT> {
	
	private static final long serialVersionUID = 1L;

	protected ListSignal<ELEMENT> _source;

	private final ListModelSetter<ELEMENT> _setter;
	
	private LabelProvider _labelProvider = new LabelProvider() {
		
		@Override
		public Signal<String> labelFor(Object element) {
			return Signals.constant(element.toString());
		}
		
		@Override
		public Signal<Image> imageFor(Object element) {
			return Signals.constant(null);
		}
	};
	
	RListImpl(ListSignal<ELEMENT> source, ListModelSetter<ELEMENT> setter) {
		_source = source;
		_setter = setter;

		initModel();
		addReceiverListener();
		initCellRenderer();
		
	}

	private void initCellRenderer() {

		ListCellRenderer cellRenderer = new ListCellRenderer(){

			@Override
			public Component getListCellRendererComponent(JList lst, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Container container = ContainerUtils.getContainer();
				RFactory rfactory = container.produce(RFactory.class);

				JPanel panel = new JPanel();
				panel.setLayout(new BorderLayout(5,5));
				
				Signal<String> slabel = _labelProvider.labelFor(value);
				Signal<Image> sicon = _labelProvider.imageFor(value);
				
				TextWidget label = rfactory.newLabel(slabel);
				ImageWidget image = rfactory.newImage(sicon);
				
				panel.add(image.getComponent(), BorderLayout.WEST);
				panel.add(label.getComponent(), BorderLayout.CENTER);
				panel.setOpaque(false);
				
				return panel;
			}
		};
		setCellRenderer(cellRenderer);
	}

	private void addReceiverListener() {
		_source.addListReceiver(new Omnivore<ListValueChange>(){
			@Override
			public void consume(ListValueChange valueObject) {
				revalidate();
				repaint();
		}});
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