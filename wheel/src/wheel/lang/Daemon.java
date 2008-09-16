package wheel.lang;

public abstract class Daemon extends Thread {

	public Daemon(String name) {
		super(name);
		setDaemon(true);
		start();
	}
	
	@Override
	public abstract void run();

}
