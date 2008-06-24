package sneer.skin.laf;

import javax.swing.JComponent;

import sneer.skin.menu.Menu;
import wheel.io.ui.Action;

public interface LafSupport extends Action{

	void initialize(Menu<JComponent> lafMenu, LafContainer root);

}