package sneer.bricks.snapps.contacts.actions;

import sneer.bricks.hardware.gui.Action;

public interface ContactAction extends Action {
	boolean isVisible();
	boolean isEnabled();
}