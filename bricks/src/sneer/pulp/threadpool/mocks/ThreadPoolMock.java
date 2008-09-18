package sneer.pulp.threadpool.mocks;

import java.util.ArrayList;
import java.util.List;

import sneer.pulp.threadpool.ThreadPool;

public class ThreadPoolMock implements ThreadPool {

	List<Runnable> _toRunList = new ArrayList<Runnable>();
	
	@Override
	public void registerActor(Runnable runnable) {
		_toRunList.add(runnable);
	}

	public void runAll(){
		for (Runnable toRun : _toRunList) {
			toRun.run();
		}
		_toRunList.clear();
	}
}
