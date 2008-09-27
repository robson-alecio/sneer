package wheel.reactive;

public interface EventNotifier<VO> {

	EventSource<VO> output();

	void notifyReceivers(VO event);
	
}
