package sneer.bricks.snapps.system.log.sysout.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.io.log.workers.notifier.LogNotifier;
import sneer.bricks.snapps.system.log.sysout.LogToSysout;
import sneer.foundation.lang.Consumer;

class LogToSysoutImpl implements LogToSysout {

	@SuppressWarnings("unused")	private final Object _referenceToAvoidGc;

	private LogToSysoutImpl(){
		_referenceToAvoidGc = my(LogNotifier.class).loggedMessages().addReceiver(new Consumer<String>(){ @Override public void consume(String msg) {
			log(msg);
		}});
	}

	private void log(String msg){
		System.out.print(msg);
	}
}