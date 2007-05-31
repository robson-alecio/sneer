package wheel.reactive.lists;

import sneer.kernel.business.chat.ChatEvent;
import wheel.lang.Omnivore;
import wheel.reactive.Receiver;

public interface ListSource<VO> {

	ListSignal<VO> output();

	//Fix: ListSource should use the omnivore instead of this
	void add(VO element);
	boolean remove(VO element);

	Omnivore<VO> adder();

}