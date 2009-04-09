package sneer.skin.snappmanager;

import sneer.brickness.OldBrick;
import wheel.reactive.lists.ListSignal;

public interface InstrumentRegistry extends OldBrick {

	void registerInstrument(Instrument instrument);

	ListSignal<Instrument> installedInstruments();
}
