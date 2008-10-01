package sneer.pulp.retrier;

public interface RetrierManager {

	Retrier startRetrier(int periodBetweenAttempts, Task task);

}
