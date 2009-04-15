package sneer.skin.old.instrumentregistry;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.collections.ListSignal;

public interface InstrumentRegistry extends OldBrick {

	void registerInstrument(OldInstrument instrument); //Fix: move to new and delete
	void registerInstrument(Instrument instrument);

	ListSignal<OldInstrument> installedOldInstruments(); //Fix: move to new and delete
	ListSignal<Instrument> installedInstruments();
}
