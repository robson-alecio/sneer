package spikes.sandro.summit.register.logger.impl;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import spikes.sandro.summit.register.SimpleRegister;
import spikes.sandro.summit.register.logger.SimpleLogger;
import static sneer.commons.environments.Environments.my;

class SimpleLoggerImpl implements SimpleLogger {

	SimpleLoggerImpl() {
		Consumer<String> receiver = new Consumer<String>() {
			@Override public void consume(String value) {
				System.out.println(value);
			}
		};

		Signal<String> output = my(SimpleRegister.class).output();
		my(Signals.class).receive(this, receiver, output);
	}
}
