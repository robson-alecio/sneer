package sneer.business;

import wheelexperiments.reactive.SetSignal;
import wheelexperiments.reactive.SetSource;

public class SneerBusiness {

	private final SetSource<String> _lives = new SetSource<String>();

	public void foster(String name) {
		_lives.add(name);
		
	}

	public SetSignal<String> lives() {
		return _lives;
	}

	public void changeName(String oldName, String newName) {
		if (!_lives.currentElements().contains(oldName)) return;
		if (_lives.currentElements().contains(newName)) return;
		_lives.remove(oldName);
		_lives.add(newName);
	}

}
