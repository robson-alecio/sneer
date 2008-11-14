package sneer.pulp.prevalence.impl;

import sneer.pulp.prevalence.StateMachine;

public class AtomicConsistentStateMachine implements StateMachine {

	private final StateMachine _business;
	private volatile Error _firstError;

	public AtomicConsistentStateMachine(StateMachine business) {
		_business = business;
	}

	@Override
	public Object changeState(Object event) {
		return performProtected(event, null);
	}

	@Override
	public Object queryState(Object query) {
		return performProtected(null, query);
	}

	private Object performProtected(Object event, Object query) {
		try {
			return perform(event, query);
		} catch (Error e) {
			keepFirstError(e);
			return null;
		} finally {
			checkError();
		}
	}

	synchronized private void keepFirstError(Error e) {
		if (_firstError != null) return;
		_firstError = e;
	}

	synchronized private void checkError() throws Error {
		if (_firstError != null)
			throw new Error("State machine is inconsistent.", _firstError);
	}
	
	private Object perform(Object event, Object query) {
		return event != null
			? _business.changeState(event)
			: _business.queryState(query);
	}
	
}
