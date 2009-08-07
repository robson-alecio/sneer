package sneer.bricks.hardware.io.log.workers.sysout.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.hardware.io.log.workers.sysout.SysoutLogWorker;

class SysoutLogWorkerImpl implements SysoutLogWorker {

	SysoutLogWorkerImpl(){
		my(Logger.class).setDelegate(new WorkerImpl());
	}
	
}
