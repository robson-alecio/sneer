package sneer.bricks.skin.menu.impl;

import static sneer.foundation.environments.Environments.my;

import javax.swing.JPopupMenu;

import sneer.bricks.skin.main.synth.menu.SynthMenus;

class MenuPopupImpl extends AbstractMenuGroup<JPopupMenu> {

	protected final JPopupMenu _menu;

	protected MenuPopupImpl() {
		_menu = my(SynthMenus.class).createMenuPopup();
	}

	@Override
	public JPopupMenu getWidget() {
		return _menu;
	}
}
