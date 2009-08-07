package sneer.bricks.hardware.io.log.workers.sysout.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.io.log.LogWorker;
import sneer.bricks.hardware.io.log.formatter.LogFormatter;

class WorkerImpl implements LogWorker {

	@Override public void log(String message, Object... messageInsets) {
		System.out.print(
			my(LogFormatter.class).format(
				message,
				messageInsets));
	}
	
}
