package sneer.pulp.logging.file.impl;

import static sneer.commons.environments.Environments.my;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sneer.commons.io.StoragePath;
import sneer.pulp.logging.Logger;
import sneer.pulp.logging.file.LogToFile;
import wheel.reactive.impl.EventReceiver;

class LogToFileImpl implements LogToFile {

	private static final String FILENAME = "sneer.log";
	private static final boolean WRITE_TO_THE_END = true;
	private final StoragePath _persistenceConfig = my(StoragePath.class);	
	private final Logger _logger = my(Logger.class);

	@SuppressWarnings("unused")
	private EventReceiver<String> _loggedMessagesReceiverAvoidGc;
	
	LogToFileImpl() {
		_loggedMessagesReceiverAvoidGc = new EventReceiver<String>(_logger.loggedMessages()) { @Override public void consume(String message) {
			appendMessage(message);
		}};
	}

	private void appendMessage(String message) {
        FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(new File(_persistenceConfig.get(), FILENAME), WRITE_TO_THE_END);
			fileWriter.write(message);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	};

}
