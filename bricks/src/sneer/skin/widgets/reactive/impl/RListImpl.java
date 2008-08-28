package sneer.skin.widgets.reactive.impl;

import static wheel.lang.Types.cast;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.RFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.ui.impl.ListSignalModel;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;

public class RListImpl<ELEMENT> extends JList implements ListWidget<ELEMENT> {
	
	private static final long serialVersionUID = 1L;

	private LabelProvider<ELEMENT> _labelProvider;

	private final ListSignal<ELEMENT> _source;
	
	RListImpl(ListSignal<ELEMENT> source, LabelProvider<ELEMENT> labelProvider) {
		_source = source;
		_labelProvider = labelProvider;
		initModel();
		addReceiverListener();
		
		
		class DefaultListCellRenderer implements ListCellRenderer{
			
			@Override
			public Component getListCellRendererComponent(JList lst, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Container container = ContainerUtils.getContainer();
				RFactory rfactory = container.produce(RFactory.class);

				JPanel panel = new JPanel();
				panel.setOpaque(false);
				panel.setLayout(new BorderLayout(5,5));
				
				Signal<String> slabel = _labelProvider.labelFor(getElement(value));
				Signal<Image> sicon = _labelProvider.imageFor(getElement(value));
				
				TextWidget<JLabel> label = rfactory.newLabel(slabel);
				panel.add(label.getComponent(), BorderLayout.CENTER);

				ImageWidget image = rfactory.newImage(sicon);
				panel.add(image.getComponent(), BorderLayout.WEST);
				panel.setOpaque(false);
				
				return panel;
			}

			private ELEMENT getElement(Object value) {
				return cast(value);
			}	
		}
		setCellRenderer(new DefaultListCellRenderer());
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
		
		setModel(new ListSignalModel<ELEMENT>(_source, new ListSignalModel.SignalChooser<ELEMENT>(){
			@Override
			public Signal<?>[] signalsToReceiveFrom(ELEMENT element) {
				return new Signal<?>[]{_labelProvider.imageFor(element), 
									   _labelProvider.labelFor(element)};
			}}));
	}

	@Override
	public JList getMainWidget() {
		return this;
	}
	
	@Override
	public JComponent getComponent(){
		return this;
	}

	@Override
	public void setLabelProvider(LabelProvider<ELEMENT> labelProvider) {
		_labelProvider = labelProvider;
	}

	@Override
	public ListSignal<ELEMENT> output() {
		return _source;
	}
}