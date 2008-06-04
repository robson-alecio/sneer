package wheel.reactive;

import wheel.lang.Omnivore;

public interface EventSource<VO> {
	public void addReceiver(Omnivore<VO> receiver);
	public void removeReceiver(Object receiver);
}
