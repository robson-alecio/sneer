package sneer.pulp.logging.out.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.logging.Logger;
import sneer.pulp.logging.out.LogToSystemOut;
import sneer.pulp.reactive.Signals;

public class LogToSystemOutImpl implements LogToSystemOut {

	{
		my(Signals.class).receive(this, new Consumer<String>() { @Override public void consume(String message) {
			System.out.println(message);
		}}, my(Logger.class).loggedMessages());
	}
	
}
