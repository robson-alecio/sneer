package sneer.pulp.prevalence.impl;

import sneer.pulp.prevalence.StateMachine;

public class AtomicConsistentStateMachine implements StateMachine {

	public static class Inconsistency extends Error {
		public Inconsistency(Throwable cause) {
			super("State machine is inconsistent.", cause);
		}
	}

	
	private final StateMachine _business;
	private volatile Throwable _firstInconsistency;

	public AtomicConsistentStateMachine(StateMachine business) {
		_business = business;
	}

	@Override
	public Object changeState(Object event) throws Inconsistency {
		return performConsistent(event, null);
	}

	@Override
	public Object queryState(Object query) throws Inconsistency {
		return performConsistent(null, query);
	}

	private Object performConsistent(Object event, Object query) throws Inconsistency {
		try {
			return perform(event, query);
		} catch (Throwable t) {
			keepFirstInconsistency(t);
			return null;
		} finally {
			checkConsistency();
		}
	}

	synchronized private void keepFirstInconsistency(Throwable t) {
		if (_firstInconsistency != null) return;
		_firstInconsistency = t;
	}

	synchronized private void checkConsistency() throws Inconsistency {
		if (_firstInconsistency != null)
			throw new Inconsistency(_firstInconsistency);
	}
	
	private Object perform(Object event, Object query) {
		return event != null
			? _business.changeState(event)
			: _business.queryState(query);
	}
	
}
