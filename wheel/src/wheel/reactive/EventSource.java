package wheel.reactive;

import wheel.lang.Omnivore;

public interface EventSource<VO> {
	public void addReceiver(Omnivore<VO> receiver);
	public void removeReceiver(Omnivore<VO> receiver);

	public void addTransientReceiver(Omnivore<VO> receiver); //Refactor: Remove this and make the prevayler.Bubble add something like a "TransientReceiverAdapter" to all signal receiver registrations on the state machine.
	public void removeTransientReceiver(Omnivore<VO> receiver);

}
