package sneer.skin.widgets.reactive.impl;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import wheel.io.ui.impl.ListSignalModel;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;
import wheel.reactive.lists.ListSignal;

public class RListImpl<ELEMENT> extends JList implements ListWidget<ELEMENT> {

	private static final long serialVersionUID = 1L;

	protected final ListSignal<ELEMENT> _source;
	protected LabelProvider<ELEMENT> _labelProvider;

	private void repaintList() {
		SwingUtilities.invokeLater(new Runnable() {@Override public void run() {
			revalidate();
			repaint();
		}});	
	}

	RListImpl(ListSignal<ELEMENT> source, LabelProvider<ELEMENT> labelProvider) {
		_source = source;
		_labelProvider = labelProvider;
		initModel();

		class DefaultListCellRenderer implements ListCellRenderer {

			private javax.swing.DefaultListCellRenderer renderer = new javax.swing.DefaultListCellRenderer();

			@Override
			public Component getListCellRendererComponent(JList ignored, Object value, int ignored2, boolean isSelected, boolean cellHasFocus) {
				
				Signal<String> slabel = _labelProvider.labelFor(getElement(value));
				Signal<Image> sicon = _labelProvider.imageFor(getElement(value));
				
				ImageIcon icon = new ImageIcon(sicon.currentValue());
				JLabel label = (JLabel) renderer.getListCellRendererComponent(ignored, value, ignored2, isSelected, cellHasFocus);
				label.setIcon(icon);
				label.setText(slabel.currentValue());
				
				@SuppressWarnings("unused")
				Receiver<Object> _listRepainter = new Receiver<Object>() {@Override	public void consume(Object ignore) {
					repaintList();
				}};

				return label;
			}

			private ELEMENT getElement(Object value) {
				return (ELEMENT)value;
			}
		}
		setCellRenderer(new DefaultListCellRenderer());

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
	public JComponent getComponent() {
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