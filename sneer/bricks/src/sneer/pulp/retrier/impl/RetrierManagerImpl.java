package sneer.pulp.retrier.impl;

import sneer.pulp.retrier.Retrier;
import sneer.pulp.retrier.RetrierManager;
import sneer.pulp.retrier.Task;

class RetrierManagerImpl implements RetrierManager {

	@Override
	public Retrier startRetrier(String threadName, int periodBetweenAttempts, Task task) {
		return new RetrierImpl(threadName, periodBetweenAttempts, task);
	}

}
