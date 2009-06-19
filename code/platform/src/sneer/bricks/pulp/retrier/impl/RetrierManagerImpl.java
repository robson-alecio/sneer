package sneer.bricks.pulp.retrier.impl;

import sneer.bricks.pulp.retrier.Retrier;
import sneer.bricks.pulp.retrier.RetrierManager;
import sneer.bricks.pulp.retrier.Task;

class RetrierManagerImpl implements RetrierManager {

	@Override
	public Retrier startRetrier(int periodBetweenAttempts, Task task) {
		return new RetrierImpl(periodBetweenAttempts, task);
	}

}
