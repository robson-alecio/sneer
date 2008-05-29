package spikes.legobricks.name;

import sneer.lego.Brick;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface PortKeeper extends Brick {

	Signal<Integer> port();

	Consumer<Integer> portSetter();

}
