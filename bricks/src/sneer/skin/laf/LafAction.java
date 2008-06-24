package sneer.skin.laf;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import sneer.skin.menu.Menu;
import wheel.io.ui.Action;

public class LafAction implements Action {

	private LookAndFeel laf;
	private LafContainer rootContainer;

	public LafAction(LookAndFeel lookAndFeel, Menu<JComponent> lafMenu, LafContainer root) {
		laf = lookAndFeel;
		this.rootContainer = root;
		lafMenu.addAction(this);
	}

	@Override
	public String caption() {
		return laf.getName();
	}

	@Override
	public void run() {
		try {
			UIManager.setLookAndFeel(laf);
			rootContainer.refreshLaf();
		} catch (UnsupportedLookAndFeelException e) {
			// ignore: same L&F
		}
	}
}