package sneer.bricks.skin.main.instrumentregistry;

import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.foundation.brickness.Brick;

@Brick
public interface InstrumentRegistry {

	void registerInstrument(Instrument instrument);

	ListSignal<Instrument> installedInstruments();
}
