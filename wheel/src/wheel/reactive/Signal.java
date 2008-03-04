package wheel.reactive;

import wheel.lang.Omnivore;
import wheel.reactive.sets.SetSignal;


/** @invariant this.toString().equals("" + this.currentValue()) */
public interface Signal<VO> extends SetSignal<VO> {  //Fix: Make Signal extend ListSignal.
	
	public void addReceiver(Omnivore<VO> receiver); //Refactor: Simply use a Consumer instead of having a Receiver interface.
	public void removeReceiver(Omnivore<VO> name);

	public void addTransientReceiver(Omnivore<VO> receiver); //Refactor: Remove this and make the prevayler.Bubble add something like a "TransientReceiverAdapter" to all signal receiver registrations on the state machine.
	public void removeTransientReceiver(Omnivore<VO> receiver);

	public VO currentValue();
	
}

