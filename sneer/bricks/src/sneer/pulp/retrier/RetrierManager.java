package sneer.pulp.retrier;

import sneer.brickness.ConcurrentBrick;

public interface RetrierManager extends ConcurrentBrick {

	Retrier startRetrier(int periodBetweenAttempts, Task task);

}
