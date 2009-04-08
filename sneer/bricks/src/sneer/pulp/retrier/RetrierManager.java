package sneer.pulp.retrier;

import sneer.brickness.Brick;

@Brick
public interface RetrierManager {

	Retrier startRetrier(int periodBetweenAttempts, Task task);

}
