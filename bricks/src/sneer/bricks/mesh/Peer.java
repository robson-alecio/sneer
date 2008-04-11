package sneer.bricks.mesh;

import wheel.reactive.Signal;

public interface Peer {

	<S> Signal<S> signal(String signalPath);

}
