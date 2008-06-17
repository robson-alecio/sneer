package sneer.bricks.threadpool;

public interface ThreadPool {

	void runDaemon(Runnable runnable);
	void registerActor(Runnable actor);

}
