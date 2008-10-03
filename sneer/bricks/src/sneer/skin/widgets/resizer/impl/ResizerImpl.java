package sneer.skin.widgets.resizer.impl;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JWindow;

import sneer.skin.widgets.resizer.Resizer;

class ResizerImpl implements Resizer {

	@Override
	public void pack(JComponent component, int maxWidth) { //Optimize - implement pack method without JWindow
		Container root = component.getParent();
		root.setPreferredSize(new Dimension(maxWidth, Integer.MAX_VALUE));
		JWindow win = new JWindow();
		win.add(root);
		win.pack();
		root.setPreferredSize(new Dimension(maxWidth, component.getPreferredSize().height));
		win.remove(root);
	}
}
