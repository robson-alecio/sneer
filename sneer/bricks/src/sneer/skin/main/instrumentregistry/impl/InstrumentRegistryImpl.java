package sneer.skin.main.instrumentregistry.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.ReactiveCollections;
import sneer.skin.main.instrumentregistry.Instrument;
import sneer.skin.main.instrumentregistry.InstrumentRegistry;

class InstrumentRegistryImpl implements InstrumentRegistry {

	ListRegister<Instrument> _instruments = my(ReactiveCollections.class).newListRegister();
	
	@Override
	public void registerInstrument(Instrument instrument) {
		_instruments.add(instrument);
	}

	@Override
	public ListSignal<Instrument> installedInstruments() {
		return _instruments.output();
	}

}
