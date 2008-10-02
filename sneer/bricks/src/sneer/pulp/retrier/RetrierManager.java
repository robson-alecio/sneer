package sneer.pulp.retrier;

import sneer.kernel.container.Brick;

public interface RetrierManager extends Brick {

	Retrier startRetrier(String threadName, int periodBetweenAttempts, Task task);

}
