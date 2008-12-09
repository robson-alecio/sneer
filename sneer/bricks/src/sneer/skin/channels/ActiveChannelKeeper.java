package sneer.skin.channels;

import sneer.kernel.container.Brick;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface ActiveChannelKeeper extends Brick {
	
	Signal<Integer> channel();
	Consumer<Integer> channelSetter();

}
