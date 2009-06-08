package spikes.sneer.pulp.prevalence;

public interface StateMachine {

	Object changeState(Object event);
	Object queryState(Object query);
	
}
