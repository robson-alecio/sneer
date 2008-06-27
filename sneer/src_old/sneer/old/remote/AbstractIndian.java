package sneer.old.remote;

import java.io.Serializable;

import sneer.old.life.LifeView;

import wheel.io.network.ObjectSocket;

abstract class AbstractIndian implements Indian, Serializable {

	private static final long serialVersionUID = 1L;

	private static int _nextId = 1;
	
	protected final int _id = _nextId++;
	
	protected transient ObjectSocket _socket;

	public int id() {
		return _id;
	}

	public String executeOn(LifeView ignored) {
		return "Ignored";
	}
}
