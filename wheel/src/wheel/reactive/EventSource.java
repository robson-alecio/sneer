package wheel.reactive;

import wheel.lang.Omnivore;

public interface EventSource<VO> {

	@Deprecated
	public void addReceiver(Omnivore<? super VO> receiver);

	@Deprecated
	public void removeReceiver(Object receiver);
}
