package sneer.bricks.pulp.retrier;

import sneer.foundation.brickness.Brick;

@Brick
public interface RetrierManager {

	Retrier startRetrier(int periodBetweenAttempts, Task task);

}
