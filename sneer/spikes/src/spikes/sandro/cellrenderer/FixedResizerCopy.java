package spikes.sandro.cellrenderer;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JWindow;

import wheel.io.ui.GuiThread;

class FixedResizerCopy {

	static JWindow win = new JWindow();
	
	static void pack(JComponent component, int maxWidth, int headerHeight ) { //Optimize - implement pack method without JWindow
		GuiThread.assertInGuiThread();
		Container root = component.getParent();
		root.setPreferredSize(new Dimension(maxWidth, Integer.MAX_VALUE));
		win.add(root);
		win.pack();
		root.setPreferredSize(new Dimension(maxWidth, component.getPreferredSize().height + headerHeight));
		win.remove(root);
	}
}
