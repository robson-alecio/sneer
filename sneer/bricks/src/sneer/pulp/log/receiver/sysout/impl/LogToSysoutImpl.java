package sneer.pulp.log.receiver.sysout.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.log.receiver.sysout.LogToSysout;
import sneer.pulp.log.workers.notifier.LogNotifier;
import sneer.pulp.reactive.Signals;

class LogToSysoutImpl implements LogToSysout {

	private LogToSysoutImpl(){
		my(Signals.class).receive(this, new Consumer<String>(){ @Override public void consume(String msg) {
			log(msg);
		}}, my(LogNotifier.class).loggedMessages());
	}
	
	private void log(String msg){
		System.out.print(msg);
	}
}