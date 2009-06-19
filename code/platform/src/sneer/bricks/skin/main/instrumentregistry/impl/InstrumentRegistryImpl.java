package sneer.bricks.skin.main.instrumentregistry.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.reactive.collections.CollectionSignals;
import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.skin.main.instrumentregistry.Instrument;
import sneer.bricks.skin.main.instrumentregistry.InstrumentRegistry;

class InstrumentRegistryImpl implements InstrumentRegistry {

	ListRegister<Instrument> _instruments = my(CollectionSignals.class).newListRegister();
	
	@Override
	public void registerInstrument(Instrument instrument) {
		_instruments.add(instrument);
	}

	@Override
	public ListSignal<Instrument> installedInstruments() {
		return _instruments.output();
	}

}
