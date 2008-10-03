package sneer.skin.widgets.reactive.impl;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;

class RListHtmlCellRenderer<ELEMENT> extends RListSimpleCellRenderer<ELEMENT>  {

	static final int scrollWidth = 20;
	private final Resizer _resizer = new Resizer();
	
	RListHtmlCellRenderer(RListImpl<ELEMENT> rlist) {
		super(rlist);
	}

	@Override
	public Component getListCellRendererComponent(JList ignored, Object value,
			int ignored2, boolean isSelected, boolean cellHasFocus) {

		Signal<String> signalText = _rlist._labelProvider.labelFor(getElement(value));
		Signal<Image> signalImage = _rlist._labelProvider.imageFor(getElement(value));

		JPanel root = new JPanel();
		root.setLayout(new GridBagLayout());
		root.setOpaque(false);

		JLabel icon = new JLabel(new ImageIcon(signalImage.currentValue()));
		icon.setOpaque(false);

		JTextArea area = new JTextArea();
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		area.setText(signalText.currentValue());

		root.add(icon, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		root.add(area, new GridBagConstraints(1, 0, 1, 1, 1., 1.,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		;

		new Receiver<Object>() {
			@Override
			public void consume(Object ignore) {
				_rlist.repaintList();
			}
		};

		_resizer.packComponent(area, _rlist.getSize().width - scrollWidth);
		addLineSpace(root);
		return root;
	}

	private void addLineSpace(JPanel root) {
		Dimension psize = root.getPreferredSize();
		root.setPreferredSize(new Dimension(psize.width, psize.height + _rlist.getLineSpace()));
	}
}
