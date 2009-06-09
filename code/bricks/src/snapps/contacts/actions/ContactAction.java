package snapps.contacts.actions;

import sneer.hardware.gui.Action;

public interface ContactAction extends Action {
	boolean isVisible();
	boolean isEnabled();
}