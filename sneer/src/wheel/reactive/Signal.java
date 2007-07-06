package wheel.reactive;

import wheel.lang.Omnivore;


/** @invariant this.toString().equals("" + this.currentValue()) */
public interface Signal<VO> extends SetSignal<VO> {  //Fix: make_signal_extend_list_signal;
	
	public void addReceiver(Omnivore<VO> receiver); //Refactor: Simply use a Consumer instead of having a Receiver interface.
	public void removeReceiver(Omnivore<VO> name);

	public void addTransientReceiver(Omnivore<VO> receiver); //Refactor: Remove this and make the prevayler.Bubble add something like a "TransientReceiverAdapter" to all signal receiver registrations on the state machine.
	public void removeTransientReceiver(Omnivore<VO> receiver);

	public VO currentValue();
	
}

