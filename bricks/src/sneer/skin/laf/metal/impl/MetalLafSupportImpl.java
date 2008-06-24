package sneer.skin.laf.metal.impl;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import sneer.skin.laf.LafContainer;
import sneer.skin.laf.metal.MetalLafSupport;
import sneer.skin.menu.Menu;

public class MetalLafSupportImpl implements MetalLafSupport {

	private LafContainer rootContainer;

	public void initialize(Menu<JComponent> lafMenu, LafContainer root) {
		this.rootContainer = root;
		lafMenu.addAction(this);
	}

	@Override
	public String caption() {
		return "Metal";
	}

	@Override
	public void run() {
		try {
			UIManager.setLookAndFeel(new MetalLookAndFeel());
			rootContainer.refreshLaf();
		} catch (UnsupportedLookAndFeelException e) {
			// ignore: using default L&F
		}
	}
}