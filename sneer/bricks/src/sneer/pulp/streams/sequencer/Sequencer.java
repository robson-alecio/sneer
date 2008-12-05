package sneer.pulp.streams.sequencer;

public interface Sequencer<T>{

	void sequence(T packet, short number);

}
