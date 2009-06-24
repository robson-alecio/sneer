package sneer.bricks.snapps.system.log.file.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sneer.bricks.hardware.io.log.workers.notifier.LogNotifier;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.snapps.system.log.file.LogToFile;
import sneer.bricks.software.directoryconfig.DirectoryConfig;
import sneer.foundation.lang.Consumer;

class LogToFileImpl implements LogToFile {

	private static final boolean WRITE_TO_THE_END = true;
	
	File _file = my(DirectoryConfig.class).logFile().get();

	@SuppressWarnings("unused")	private final Object _referenceToAvoidGc;

	private LogToFileImpl(){
		_referenceToAvoidGc = my(Signals.class).receive(my(LogNotifier.class).loggedMessages(), new Consumer<String>(){ @Override public void consume(String msg) {
			log(msg);
		}});
	}
	
	private void log(String msg){
        FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(_file, WRITE_TO_THE_END);
			fileWriter.write(msg);
			fileWriter.flush();
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e);
		} finally{
			try { fileWriter.close(); } catch (Exception ignore) {}
		}
	}
}