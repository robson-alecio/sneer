package sneer.skin.snappmanager;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.collections.ListSignal;

public interface InstrumentRegistry extends OldBrick {

	void registerInstrument(Instrument instrument);

	ListSignal<Instrument> installedInstruments();
}
