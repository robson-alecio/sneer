package sneer.pulp.retrier.impl;

import sneer.pulp.retrier.Retrier;
import sneer.pulp.retrier.RetrierManager;
import sneer.pulp.retrier.Task;

class RetrierManagerImpl implements RetrierManager {

	@Override
	public Retrier startRetrier(int periodBetweenAttempts, Task task) {
		return new RetrierImpl(periodBetweenAttempts, task);
	}

}
