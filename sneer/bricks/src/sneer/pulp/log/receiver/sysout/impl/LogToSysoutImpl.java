package sneer.pulp.log.receiver.sysout.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.log.receiver.sysout.LogToSysout;
import sneer.pulp.log.workers.notifier.LogNotifier;
import sneer.pulp.reactive.Signals;

class LogToSysoutImpl implements LogToSysout {

	@SuppressWarnings("unused")	private final Object _referenceToAvoidGc;

	private LogToSysoutImpl(){
		_referenceToAvoidGc = my(Signals.class).receive(new Consumer<String>(){ @Override public void consume(String msg) {
			log(msg);
		}}, my(LogNotifier.class).loggedMessages());
	}

	private void log(String msg){
		System.out.print(msg);
	}
}