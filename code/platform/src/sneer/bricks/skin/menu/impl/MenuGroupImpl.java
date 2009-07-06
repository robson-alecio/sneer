package sneer.bricks.skin.menu.impl;

import static sneer.foundation.environments.Environments.my;

import javax.swing.JMenu;

import sneer.bricks.skin.main.synth.menu.SynthMenus;

class MenuGroupImpl extends AbstractMenuGroup<JMenu> {

	protected final JMenu _menu;
	
	MenuGroupImpl(String text) {
		_menu = my(SynthMenus.class).createMenuGroup();
		_menu.setText(text);
	}

	@Override
	public JMenu getWidget() {
		return _menu;
	}
}