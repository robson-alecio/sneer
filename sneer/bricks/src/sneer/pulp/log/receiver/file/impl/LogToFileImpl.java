package sneer.pulp.log.receiver.file.impl;

import static sneer.commons.environments.Environments.my;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sneer.brickness.StoragePath;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.log.receiver.file.LogToFile;
import sneer.pulp.log.workers.notifier.LogNotifier;
import sneer.pulp.reactive.Signals;

class LogToFileImpl implements LogToFile {

	private static final String FILENAME = "sneer.log";
	private static final boolean WRITE_TO_THE_END = true;
	
	private final StoragePath _persistenceConfig = my(StoragePath.class);	
	File _file = new File(_persistenceConfig.get(), FILENAME);
	
	private LogToFileImpl(){
		System.out.println("Log File: " + _file.getAbsolutePath());
		my(Signals.class).receive(this, new Consumer<String>(){ @Override public void consume(String msg) {
			log(msg);
		}}, my(LogNotifier.class).loggedMessages());
	}
	
	private void log(String msg){
        FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(_file, WRITE_TO_THE_END);
			fileWriter.write(msg);
			fileWriter.flush();
		} catch (IOException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e);
		} finally{
			try { fileWriter.close(); } catch (Exception ignore) {}
		}
	}
}