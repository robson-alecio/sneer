package sneer.skin.main.instrumentregistry;

import sneer.brickness.Brick;
import sneer.pulp.reactive.collections.ListSignal;

@Brick
public interface InstrumentRegistry {

	void registerInstrument(Instrument instrument);

	ListSignal<Instrument> installedInstruments();
}
