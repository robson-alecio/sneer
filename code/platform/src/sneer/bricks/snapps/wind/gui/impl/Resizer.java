package sneer.bricks.snapps.wind.gui.impl;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JWindow;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import static sneer.foundation.environments.Environments.my;

class Resizer {

	static JWindow win = new JWindow();
	
	static void pack(JComponent component, int maxWidth, int headerHeight ) { //Optimize - implement pack method without JWindow
		my(GuiThread.class).assertInGuiThread();
		Container root = component.getParent();
		root.setPreferredSize(new Dimension(maxWidth, Integer.MAX_VALUE));
		win.add(root);
		win.pack();
		root.setPreferredSize(new Dimension(maxWidth, component.getPreferredSize().height + headerHeight));
		win.remove(root);
	}
}
