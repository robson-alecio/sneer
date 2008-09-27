package spikes.gandhi.cpu;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public Main() {
		RuntimeMXBean runtimeMBean = ManagementFactory.getRuntimeMXBean();
		ThreadMXBean threadMBean = ManagementFactory.getThreadMXBean();

		new NormalTask("FIRST_TASK").start();
		new NormalTask("SECOND_TASK").start();
		new IntensiveTask("INTENSIVE_TASK").start();

		while(true){
			MyThreadsInfo threadsElement = new MyThreadsInfo();
			populate(runtimeMBean, threadsElement, threadMBean);
			System.out.println(threadsElement);
			try{Thread.sleep(10000);}catch(Exception e){}
		}
	}
	
	class NormalTask extends Thread{
		public NormalTask(String name){
			setName(name);
		}
		
		@Override
		public void run(){
			while(true){
				//do some calculation
				for(int t=0;t<10;t++){
					double x=Math.random();
					x=0+x; //avoid warning.
				}
				Thread.yield();
			}
		}
		
	}
	
	class IntensiveTask extends Thread{
		public IntensiveTask(String name){
			setName(name);
		}
		
		@Override
		public void run(){
			while(true){
				//do some calculation
				for(int t=0;t<100;t++){
					double x=Math.random()*2000/312321321/Math.random();
					x=0+x; //avoid warning.
				}
				Thread.yield();
			}
		}
		
	}

	private void populate(RuntimeMXBean runtime, MyThreadsInfo threadsElement, ThreadMXBean threads) {
		threadsElement._thread_count = threads.getThreadCount();
		threadsElement._total_started_thread_count = threads.getDaemonThreadCount();
		threadsElement._peak_thread_count = threads.getPeakThreadCount();
		long totalCpuTime = 0l;
		long totalUserTime = 0l;

		// Parse each thread
		ThreadInfo[] threadInfos = threads.getThreadInfo(threads.getAllThreadIds());
		for (int i = 0; i < threadInfos.length; i++) {

			MyThreadInfo threadElement = new MyThreadInfo();
			threadElement._id = threadInfos[i].getThreadId();
			threadElement._name = threadInfos[i].getThreadName();
			threadElement._cpu_time_nano = threads.getThreadCpuTime(threadInfos[i].getThreadId());
			threadElement._cpu_time_ms = threads.getThreadCpuTime(threadInfos[i].getThreadId()) / 1000000l;
			threadElement._user_time_nano = threads.getThreadUserTime(threadInfos[i].getThreadId());
			threadElement._user_time_ms = threads.getThreadUserTime(threadInfos[i].getThreadId()) / 1000000l;
			threadElement._blocked_count = threadInfos[i].getBlockedCount();
			threadElement._blocked_time = threadInfos[i].getBlockedTime();
			threadElement._waited_count = threadInfos[i].getWaitedCount();
			threadElement._waited_time = threadInfos[i].getWaitedTime();
			threadsElement.add(threadElement);

			// Update our aggregate values
			totalCpuTime += threads.getThreadCpuTime(threadInfos[i].getThreadId());
			totalUserTime += threads.getThreadUserTime(threadInfos[i].getThreadId());
		}
		long totalCpuTimeMs = totalCpuTime / 1000000l;
		long totalUserTimeMs = totalUserTime / 1000000l;
		threadsElement._total_cpu_time_nano = totalCpuTime;
		threadsElement._total_user_time_nano = totalUserTime;
		threadsElement._total_cpu_time_ms = totalCpuTimeMs;
		threadsElement._total_user_time_ms = totalUserTimeMs;

		// Compute thread percentages
		long uptime = runtime.getUptime();
		threadsElement._uptime = uptime;
		double cpuPercentage = ((double) totalCpuTimeMs / (double) uptime) * 100.0;
		double userPercentage = ((double) totalUserTimeMs / (double) uptime) * 100.0;
		threadsElement._total_cpu_percent = cpuPercentage;
		threadsElement._total_user_percent = userPercentage;
	}

	class MyThreadsInfo {
		long _thread_count;
		long _total_started_thread_count;
		long _daemon_thread_count;
		long _peak_thread_count;
		long _total_cpu_time_nano;
		long _total_user_time_nano;
		long _total_cpu_time_ms;
		long _total_user_time_ms;
		long _uptime;
		double _total_cpu_percent;
		double _total_user_percent;

		List<MyThreadInfo> infos = new ArrayList<MyThreadInfo>();

		public void add(MyThreadInfo info) {
			infos.add(info);
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("total cpu:" + _total_cpu_time_ms + "\n");
			builder.append("-------Thread List--------" + "\n");
			for (MyThreadInfo info : infos)
				builder.append(info.toString() + " percent:"+ (info._cpu_time_ms*100/_total_cpu_time_ms)+"\n");
			builder.append("--------------------------" + "\n");
			return builder.toString();
		}

	}

	class MyThreadInfo {
		long _id;
		String _name;
		long _cpu_time_nano;
		long _cpu_time_ms;
		long _user_time_nano;
		long _user_time_ms;
		long _blocked_time;
		long _blocked_count;
		long _waited_count;
		long _waited_time;
		@Override
		public String toString() {
			return "Thread " + _name + " / time = " + _cpu_time_ms;
		}
	}

	public static void main(String[] args) {
		new Main();
	}

}
