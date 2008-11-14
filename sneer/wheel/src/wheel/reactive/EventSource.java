package wheel.reactive;

import wheel.lang.Consumer;

public interface EventSource<VO> {

	public void addReceiver(Consumer<? super VO> receiver);
	public void removeReceiver(Object receiver);
}
