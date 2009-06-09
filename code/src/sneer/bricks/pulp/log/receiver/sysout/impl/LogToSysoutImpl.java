package sneer.bricks.pulp.log.receiver.sysout.impl;

import static sneer.foundation.commons.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.log.receiver.sysout.LogToSysout;
import sneer.bricks.pulp.log.workers.notifier.LogNotifier;
import sneer.bricks.pulp.reactive.Signals;

class LogToSysoutImpl implements LogToSysout {

	@SuppressWarnings("unused")	private final Object _referenceToAvoidGc;

	private LogToSysoutImpl(){
		_referenceToAvoidGc = my(Signals.class).receive(my(LogNotifier.class).loggedMessages(), new Consumer<String>(){ @Override public void consume(String msg) {
			log(msg);
		}});
	}

	private void log(String msg){
		System.out.print(msg);
	}
}