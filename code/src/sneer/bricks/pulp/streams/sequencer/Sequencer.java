package sneer.bricks.pulp.streams.sequencer;

public interface Sequencer<T>{

	void produceInSequence(T element, short number);

}
