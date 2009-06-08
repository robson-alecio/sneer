package spikes.vitor.threads;

public class Main {

	public static void main(String[] args) {
		BadThread bad = new BadThread();
		
		bad.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Killing");
		// bad.destroy(); 						// No stops
		// bad.interrupt(); 					// No stops
		// bad.stop(); 							// YES!!
		// bad.stop(new Exception("Go out!")); 	// YES!!
		// bad.suspend();						// YES!!
		
		System.out.println("Killed");
	}
	
}
