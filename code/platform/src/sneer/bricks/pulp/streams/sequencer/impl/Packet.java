package sneer.bricks.pulp.streams.sequencer.impl;

class Packet<T> {

	final T _payload;
	final short _sequence;

	public Packet(T payload, short sequence) {
		_payload = payload;
		_sequence = sequence;
	}

	@Override
	public String toString() {
		return "" + _sequence;		
	}
	
}
