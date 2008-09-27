package sneer.skin.snappmanager;

import sneer.kernel.container.Brick;
import wheel.reactive.lists.ListSignal;

public interface InstrumentManager extends Brick {

	void registerInstrument(Instrument instrument);

	ListSignal<Instrument> installedInstruments();
}
