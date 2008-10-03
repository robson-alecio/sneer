package sneer.skin.widgets.reactive.impl;

import java.awt.Component;
import java.awt.Image;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;

class RListSimpleCellRenderer<ELEMENT> implements ListCellRenderer {

	protected final RListImpl<ELEMENT> _rlist;
	private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();

	RListSimpleCellRenderer(RListImpl<ELEMENT> rlist) {
		_rlist = rlist;
	}
	
	@Override
	public Component getListCellRendererComponent(JList ignored, Object value, int ignored2, boolean isSelected, boolean cellHasFocus) {

		Signal<String> slabel = _rlist._labelProvider.labelFor(getElement(value));
		Signal<Image> sicon = _rlist._labelProvider.imageFor(getElement(value));

		ImageIcon icon = new ImageIcon(sicon.currentValue());
		JLabel label = (JLabel) renderer.getListCellRendererComponent(ignored, 	value, ignored2, isSelected, cellHasFocus);
		label.setIcon(icon);
		label.setText(slabel.currentValue());

		@SuppressWarnings("unused")
		Receiver<Object> _listRepainter = new Receiver<Object>() {
			@Override
			public void consume(Object ignore) {
				_rlist.repaintList();
			}
		};

		return label;
	}

	protected ELEMENT getElement(Object value) {
		return (ELEMENT) value;
	}
}