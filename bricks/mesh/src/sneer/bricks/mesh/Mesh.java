package sneer.bricks.mesh;

import wheel.reactive.Signal;

public interface Mesh {

	<T> Signal<T> findSignal(String nicknamePath, String signalPath);

}
