package spikes.vitor.threads;

public class BadThread extends Thread{

	@Override
	public void run() {
		while (true) {
			System.out.println("Hi Idiot!!");
			try {
				sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}
	
}
