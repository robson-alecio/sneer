package sneer.bricks.mesh;

import wheel.reactive.Signal;

public interface Party {

	Party navigateTo(String nickname);

	<S> Signal<S> signal(String signalPath);

}
