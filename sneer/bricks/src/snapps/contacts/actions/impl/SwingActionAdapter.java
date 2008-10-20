package snapps.contacts.actions.impl;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import snapps.contacts.actions.ContactAction;
import sneer.pulp.contacts.Contact;

public class SwingActionAdapter implements ContactAction, javax.swing.Action {

	protected final ContactAction _action;
	protected Contact _activeContact;

	public SwingActionAdapter(ContactAction action) {
		_action = action;
	}

	@Override
	public void setActive(Contact contact) {
		_activeContact = contact;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		run();
	}	
	
	@Override public void setEnabled(boolean b) {/*ignore*/	}
	@Override public void addPropertyChangeListener(PropertyChangeListener listener) {/*ignore*/}
	@Override public void removePropertyChangeListener(PropertyChangeListener listener) { /*ignore*/}
	@Override public Object getValue(String key) {	/*ignore*/ return null;}
	@Override public void putValue(String key, Object value) { /*ignore*/ }

	@Override
	public boolean isEnabled() {
		return _action.isEnabled();
	}

	@Override
	public boolean isVisible() {
		return _action.isVisible();
	}

	@Override
	public String caption() {
		return _action.caption();
	}

	@Override
	public void run() {
		_action.run();
	}
}