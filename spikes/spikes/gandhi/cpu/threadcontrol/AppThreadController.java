package spikes.gandhi.cpu.threadcontrol;
import java.util.Hashtable;
import java.util.Map;


public class AppThreadController extends Thread{

	private Map<String,AppThread> _appThreads = new Hashtable<String,AppThread>();
	
	public AppThreadController(){
		setDaemon(true);
	}
	
	public void start(String id, AppThread appThread, int cpuLimit){
		_appThreads.put(id,appThread);
		appThread._cpuUsagePercentLimit = cpuLimit;
		appThread.start();
	}
	
	public void setCpuLimit(String id, int cpuLimit){
		AppThread appThread = _appThreads.get(id);
		if (appThread == null) return;
		appThread._cpuUsagePercentLimit = cpuLimit;
	}
	
	@Override
	public void run(){
		while(true){
			for(String id:_appThreads.keySet()){
				AppThread appThread = _appThreads.get(id);
				System.out.println("id: "+ id +" - load: "+appThread.cpuUsagePercentAverage()+" - "+appThread._throttleRate);
			}
			System.out.println("---------------------------------------------------");
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
}
