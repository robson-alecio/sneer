package sneer.skin.widgets.reactive.impl;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import sneer.skin.widgets.resizer.Resizer;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;

class RListHtmlCellRenderer<ELEMENT> extends RListSimpleCellRenderer<ELEMENT>  {

	static final int scrollWidth = 20;
	private final Resizer _resizer;
	
	RListHtmlCellRenderer(RListImpl<ELEMENT> rlist, Resizer resizer) {
		super(rlist);
		_resizer = resizer;
	}

	@Override
	public Component getListCellRendererComponent(JList ignored, Object value,
			int ignored2, boolean isSelected, boolean cellHasFocus) {

		JComponent icon = createIcon(value);
		JComponent area = createText(value);
		JComponent root = createRootPanel(icon, area);

		new Receiver<Object>() {
			@Override
			public void consume(Object ignore) {
				_rlist.repaintList();
			}
		};

		_resizer.pack(area, _rlist.getSize().width - scrollWidth);
		addLineSpace(root);
		return root;
	}
	
	private JComponent createIcon(Object value) {
		Signal<Image> signalImage = _rlist._labelProvider.imageFor(getElement(value));
		JLabel icon = new JLabel(new ImageIcon(signalImage.currentValue()));
		icon.setOpaque(false);
		return icon;
	}
	
	private JComponent createText(Object value) {
		Signal<String> signalText = _rlist._labelProvider.labelFor(getElement(value));
		JTextArea area = new JTextArea();
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		area.setText(signalText.currentValue());
		return area;
	}

	private JComponent createRootPanel(JComponent icon, JComponent area) {
		JPanel root = new JPanel();
		root.setLayout(new GridBagLayout());
		root.setOpaque(false);

		root.add(icon, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		root.add(area, new GridBagConstraints(1, 0, 1, 1, 1., 1.,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		return root;
	}

	private void addLineSpace(JComponent root) {
		Dimension psize = root.getPreferredSize();
		root.setPreferredSize(new Dimension(psize.width, psize.height + _rlist.getLineSpace()));
	}
}
