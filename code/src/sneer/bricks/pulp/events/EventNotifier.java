package sneer.bricks.pulp.events;

public interface EventNotifier<VO> {

	EventSource<VO> output();

	void notifyReceivers(VO event);
	
}
