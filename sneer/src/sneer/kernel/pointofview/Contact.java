package sneer.kernel.pointofview;

import wheel.reactive.Signal;

public interface Contact {

	Signal<String> nick();
	Party party();
	
}
