package sneer.pulp.retrier;

import sneer.kernel.container.ConcurrentBrick;

public interface RetrierManager extends ConcurrentBrick {

	Retrier startRetrier(int periodBetweenAttempts, Task task);

}
