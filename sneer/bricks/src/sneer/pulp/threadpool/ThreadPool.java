package sneer.pulp.threadpool;

public interface ThreadPool {

	void registerActor(Runnable runnable);
	void registerStepper(Stepper stepper);

}
