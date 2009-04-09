package snapps.contacts.actions;

import wheel.io.ui.action.Action;

public interface ContactAction extends Action {
	boolean isVisible();
	boolean isEnabled();
}