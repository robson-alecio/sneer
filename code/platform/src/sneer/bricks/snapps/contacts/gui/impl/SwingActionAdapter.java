package sneer.bricks.snapps.contacts.gui.impl;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import sneer.bricks.snapps.contacts.actions.ContactAction;

class SwingActionAdapter implements javax.swing.Action {

	protected final ContactAction _delegate;

	public SwingActionAdapter(ContactAction delegate) {
		_delegate = delegate;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		_delegate.run();
	}	
	
	@Override public void setEnabled(boolean b) {/*ignore*/	}
	@Override public void addPropertyChangeListener(PropertyChangeListener listener) {/*ignore*/}
	@Override public void removePropertyChangeListener(PropertyChangeListener listener) { /*ignore*/}
	@Override public void putValue(String key, Object value) { /*ignore*/ }

	@Override
	public boolean isEnabled() {
		return _delegate.isEnabled();
	}

	@Override public Object getValue(String key) {
		return Action.NAME.equals(key)
			? _delegate.caption()
			: null;
	}

}