package sneer.bricks.skin.main.synth.menu.impl;

import static sneer.foundation.environments.Environments.my;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.skin.main.synth.Synth;
import sneer.bricks.skin.main.synth.menu.SynthMenus;

class SynthMenusImpl implements SynthMenus {
	
	SynthMenusImpl(){
		my(Synth.class).loadForWussies(this.getClass());
	}

	@Override public JMenuBar createMenuBar(){ return attach(new JMenuBar()); }
	@Override public JMenu createMenuGroup() { return attach(new JMenu()); }
	@Override public JMenuItem createMenuItem() { return attach(new JMenuItem()); }
	@Override public JPopupMenu createMenuPopup() { return attach(new JPopupMenu()); }

	private <T extends JComponent> T attach(final T component) {
		my(GuiThread.class).invokeAndWaitForWussies(new Runnable(){ @Override public void run() {
			my(Synth.class).attach(component);
		}});
		return component;
	}

}