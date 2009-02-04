package sneer.pulp.logging.impl;

import static wheel.lang.Environments.my;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sneer.pulp.config.persistence.PersistenceConfig;
import sneer.pulp.logging.LogToFile;
import sneer.pulp.logging.Logger;
import wheel.reactive.impl.Receiver;

class LogToFileImpl implements LogToFile {

	private static final String FILENAME = "sneer.log";
	private static final boolean WRITE_TO_THE_END = true;
	private final PersistenceConfig _persistenceConfig = my(PersistenceConfig.class);	
	private final Logger _logger = my(Logger.class);

	@SuppressWarnings("unused")
	private Receiver<String> _loggedMessagesReceiverAvoidGc;
	
	LogToFileImpl() {
		_loggedMessagesReceiverAvoidGc = new Receiver<String>(_logger.loggedMessages()) { @Override public void consume(String message) {
			appendMessage(message);
		}};
	}

	private void appendMessage(String message) {
        FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(new File(_persistenceConfig.persistenceDirectory(), FILENAME), WRITE_TO_THE_END);
			fileWriter.write(message);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	};

}
