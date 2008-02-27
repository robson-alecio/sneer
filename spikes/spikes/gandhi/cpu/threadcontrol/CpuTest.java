package spikes.gandhi.cpu.threadcontrol;


public class CpuTest {

	public static void main(String[] args){
		
		TestThread appThread1 = new TestThread();
		TestThread appThread2 = new TestThread();
		TestThread appThread3 = new TestThread();
		
		AppThreadController controller = new AppThreadController();
		controller.start();
		controller.start("teste1", appThread1, 5);
		controller.start("teste2", appThread2, 15);
		controller.start("teste3", appThread3, 35);

		//uses 55% of the cpu for a minute
		try {Thread.sleep(60000);} catch (InterruptedException e) {}
		
		controller.setCpuLimit("teste1", 10);
		controller.setCpuLimit("teste2", 10);
		controller.setCpuLimit("teste3", 10);
		//uses 30% of the cpu for a minute
		try {Thread.sleep(60000);} catch (InterruptedException e) {}

	}
	
	public static class TestThread extends AppThread{

		@Override
		public void run() {
			while(true){
				//do anything
				@SuppressWarnings("unused")
				double x=Math.random()*Math.random()/Math.random();
				//do anything
				throttle();
			}
		}
		
	}
	
}
