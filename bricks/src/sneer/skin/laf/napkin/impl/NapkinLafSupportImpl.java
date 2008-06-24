package sneer.skin.laf.napkin.impl;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.sourceforge.napkinlaf.NapkinLookAndFeel;
import sneer.skin.laf.LafContainer;
import sneer.skin.laf.napkin.NapkinLafSupport;
import sneer.skin.menu.Menu;

public class NapkinLafSupportImpl implements NapkinLafSupport {

	private LafContainer rootContainer;

	public void initialize(Menu<JComponent> lafMenu, LafContainer root) {
		this.rootContainer = root;
		lafMenu.addAction(this);
	}

	@Override
	public String caption() {
		return "Naplin";
	}

	@Override
	public void run() {
		try {
			UIManager.setLookAndFeel(new NapkinLookAndFeel());
			rootContainer.refreshLaf();
		} catch (UnsupportedLookAndFeelException e) {
			// ignore: using default L&F
		}
	}
}