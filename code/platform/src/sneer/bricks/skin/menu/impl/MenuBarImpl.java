package sneer.bricks.skin.menu.impl;

import static sneer.foundation.environments.Environments.my;

import javax.swing.JMenuBar;

import sneer.bricks.skin.main.synth.menu.SynthMenus;

class MenuBarImpl extends AbstractMenuGroup<JMenuBar> {

	protected final JMenuBar _bar;

	protected MenuBarImpl() {
		_bar = my(SynthMenus.class).createMenuBar();
	}

	@Override
	public JMenuBar getWidget() {
		return _bar;
	}
}
