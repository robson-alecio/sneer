package sneer.skin.snappmanager.impl;

import sneer.skin.snappmanager.Instrument;
import sneer.skin.snappmanager.InstrumentManager;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

class InstrumentManagerImpl implements InstrumentManager {

	ListRegister<Instrument> _instruments = new ListRegisterImpl<Instrument>();
	
	@Override
	public void registerInstrument(Instrument instrument) {
		_instruments.add(instrument);
	}

	@Override
	public ListSignal<Instrument> installedInstruments() {
		return _instruments.output();
	}

}
