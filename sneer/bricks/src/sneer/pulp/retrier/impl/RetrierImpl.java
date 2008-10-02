package sneer.pulp.retrier.impl;

import sneer.pulp.retrier.Retrier;
import sneer.pulp.retrier.Task;
import wheel.lang.Daemon;

class RetrierImpl implements Retrier {

	RetrierImpl(String threadName, @SuppressWarnings("unused") int periodBetweenAttempts, @SuppressWarnings("unused") Task task) {
		new Daemon(threadName) { @Override public void run() {
			throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
		}};
	}

	@Override
	public void giveUpIfStillTrying() {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

}
