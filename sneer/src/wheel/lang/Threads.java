package wheel.lang;


public class Threads {

	public static void waitWithoutInterruptions(Object object) {
		try {
			object.wait();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void sleepWithoutInterruptions(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}


    public static void startDaemon(Runnable runnable) {
        Thread daemon = new Thread(runnable);
        daemon.setDaemon(true);
        daemon.start();
    }

}
