package snapps.wind;

import sneer.kernel.container.Brick;
import wheel.lang.Consumer;
import wheel.reactive.lists.ListSignal;

public interface Wind extends Brick {

	ListSignal<Shout> shoutsHeard();

	Consumer<String> megaphone();

}
