package sneer.kernel.gui.contacts;

import javax.swing.tree.DefaultTreeModel;

import wheel.lang.Omnivore;

public class DisplaySignalReceiver<U> implements Omnivore<U>{

	private final DefaultTreeModel _model;
	private final FriendNode _friend;

	public DisplaySignalReceiver(DefaultTreeModel model, FriendNode friend){
		_model = model;
		_friend = friend;
	}
	
	public void consume(U ignored) {
		_model.nodeChanged(_friend);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((_friend == null) ? 0 : _friend.hashCode());
		result = PRIME * result + ((_model == null) ? 0 : _model.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DisplaySignalReceiver other = (DisplaySignalReceiver) obj;
		if (_friend == null) {
			if (other._friend != null)
				return false;
		} else if (!_friend.equals(other._friend))
			return false;
		if (_model == null) {
			if (other._model != null)
				return false;
		} else if (!_model.equals(other._model))
			return false;
		return true;
	}

}
