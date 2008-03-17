package sneer.bricks.mesh;

import wheel.reactive.Signal;

public interface Mesh {

	Signal<String> findSignal(String nicknamePath, String string);

}
