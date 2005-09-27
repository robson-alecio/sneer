package sneer.remote;

import java.io.Serializable;

import sneer.life.LifeView;

import wheel.experiments.environment.network.ObjectSocket;

abstract class AbstractIndian implements Indian, Serializable {

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
