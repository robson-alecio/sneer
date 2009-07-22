package sneer.bricks.skin.widgets.reactive.impl;

import java.awt.Component;
import java.awt.Image;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import sneer.bricks.pulp.reactive.Signal;


class RListSimpleCellRenderer<ELEMENT> implements ListCellRenderer {

	protected final RListImpl<ELEMENT> _rlist;
	private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();

	RListSimpleCellRenderer(RListImpl<ELEMENT> rlist) {
		_rlist = rlist;
	}
	
	@Override
	public Component getListCellRendererComponent(JList ignored, Object value, int ignored2, boolean isSelected, boolean cellHasFocus) {

		Signal<String> slabel = _rlist._labelProvider.labelFor(getElement(value));
		JLabel label = (JLabel) renderer.getListCellRendererComponent(ignored, value, ignored2, isSelected, cellHasFocus);
		label.setText(slabel.currentValue());

		Signal<? extends Image> sicon = _rlist._labelProvider.imageFor(getElement(value));
		if (sicon.currentValue() != null) {
			ImageIcon icon = new ImageIcon(sicon.currentValue());
			label.setIcon(icon);
		}

		return label;
	}

	protected ELEMENT getElement(Object value) {
		return (ELEMENT) value;
	}
}