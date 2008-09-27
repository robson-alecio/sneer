package spikes.gandhi.cpu.threadcontrol;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public abstract class AppThread extends Thread {
	private ThreadMXBean _mxBean;

	public AppThread() {
		setDaemon(true);
		_mxBean = ManagementFactory.getThreadMXBean();
		if (!_mxBean.isCurrentThreadCpuTimeSupported())
			throw new IllegalStateException("CPU Usage monitoring is not avaliable!");
		_mxBean.setThreadCpuTimeEnabled(true);
	}

	@Override
	public abstract void run();

	public long userTime() {
		return _mxBean.getThreadCpuTime(getId());
	}

	public int _cpuUsagePercentLimit=100;
	
	private long _lastCpuTime = System.nanoTime();
	private long _lastUserTime;
	private double _cpuUsagePercent;
	private long _startThrottleInterval;
	
	public static final long INTERVAL = 500000000; //500 millis
	public static final long SUB_INTERVAL = INTERVAL/500; //1 millis 
	public static final long THROTTLE_DELAY_IN_MILLIS = 10; //10 milli
	public long _throttleRate = 20*SUB_INTERVAL; //20 millis

	public void throttle() {
		long cpuTime = System.nanoTime();
		long elapsedCpuTime = cpuTime - _lastCpuTime;

		if (elapsedCpuTime > INTERVAL) { //check percent usage loop
			long userTime = userTime();
			long elapsedUserTime = (userTime - _lastUserTime);
			_cpuUsagePercent = ((elapsedUserTime * 100.0) / elapsedCpuTime);
			
			updateAverageCounter();
			
			double cpuDiff = _cpuUsagePercentLimit - _cpuUsagePercent; //uses positive and negative values
			_throttleRate+=cpuDiff*SUB_INTERVAL; //rate equalizer response is proportional to difference, faster response for big diffs

			_lastCpuTime = System.nanoTime(); 
			_lastUserTime = userTime();

		}
		long elapsedThrottleInterval = cpuTime - _startThrottleInterval;
		if (elapsedThrottleInterval > _throttleRate) { //throttle loop
			try {
				Thread.sleep(THROTTLE_DELAY_IN_MILLIS);
			} catch (InterruptedException e) {
			}
			_startThrottleInterval = cpuTime;
		}

	}

	private void updateAverageCounter() { //tries to keep an average at every 5-10 samples
		_cpuUsagePercentAverage+=_cpuUsagePercent;
		_averageCounter++;
		if (_averageCounter==10){
			_cpuUsagePercentAverage = _cpuUsagePercentAverage/2.0;
			_averageCounter = 5;
		}
	}
	
	private double _cpuUsagePercentAverage;
	private int _averageCounter;

	public double cpuUsagePercentAverage() { //cpuPercent is too unstable, getting an average makes it more usable for outside classes
		if (_averageCounter==0) return 0;
		return _cpuUsagePercentAverage/_averageCounter;
	}

}
