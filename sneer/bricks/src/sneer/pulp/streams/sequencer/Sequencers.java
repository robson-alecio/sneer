package sneer.pulp.streams.sequencer;

import sneer.kernel.container.Brick;
import wheel.lang.Consumer;

public interface Sequencers extends Brick {

	<T> Sequencer<T> createSequencerFor(Consumer<T> consumer);
	
}
