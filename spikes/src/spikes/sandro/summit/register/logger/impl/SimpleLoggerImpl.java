package spikes.sandro.summit.register.logger.impl;

import static sneer.foundation.commons.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import spikes.sandro.summit.register.SimpleRegister;
import spikes.sandro.summit.register.logger.SimpleLogger;

class SimpleLoggerImpl implements SimpleLogger {

	@SuppressWarnings("unused") private final Object _referenceToAvoidGc;

	SimpleLoggerImpl() {
		Consumer<String> receiver = new Consumer<String>() {
			@Override public void consume(String value) {
				System.out.println(value);
			}
		};

		Signal<String> output = my(SimpleRegister.class).output();
		_referenceToAvoidGc = my(Signals.class).receive(output, receiver);
	}
}
