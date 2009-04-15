package sneer.skin.old.snappmanager;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.collections.ListSignal;

public interface InstrumentRegistry extends OldBrick {

	void registerInstrument(OldInstrument instrument);

	ListSignal<OldInstrument> installedInstruments();
}
