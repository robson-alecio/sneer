package sneer.bricks.mesh;

import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public interface Party {

	<T> Party navigateTo(String nickname) throws IllegalParameter;

	<S> Signal<S> signal(String signalPath);

}
