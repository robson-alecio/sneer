package sneer.skin.old.instrumentregistry.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.ReactiveCollections;
import sneer.skin.old.instrumentregistry.Instrument;
import sneer.skin.old.instrumentregistry.InstrumentRegistry;
import sneer.skin.old.instrumentregistry.OldInstrument;

class InstrumentRegistryImpl implements InstrumentRegistry {

	ListRegister<OldInstrument> _oldInstruments = my(ReactiveCollections.class).newListRegister();
	ListRegister<Instrument> _instruments = my(ReactiveCollections.class).newListRegister();
	
	@Override
	public void registerInstrument(OldInstrument instrument) {
		_oldInstruments.add(instrument);
	}

	@Override
	public ListSignal<OldInstrument> installedOldInstruments() {
		return _oldInstruments.output();
	}

	@Override
	public void registerInstrument(Instrument instrument) {
		_instruments.add(instrument);
	}

	@Override
	public ListSignal<Instrument> installedInstruments() {
		return _instruments.output();
	}

}
