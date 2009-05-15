package sneer.pulp.log.receiver.sysout.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.log.receiver.sysout.LogToSysOut;
import sneer.pulp.log.workers.notifier.LogNotifier;
import sneer.pulp.reactive.Signals;

class LogToSysOutImpl implements LogToSysOut {

	private LogToSysOutImpl(){
		my(Signals.class).receive(this, new Consumer<String>(){ @Override public void consume(String msg) {
			log(msg);
		}}, my(LogNotifier.class).loggedMessages());
	}
	
	private void log(String msg){
		System.out.print(msg);
	}
}