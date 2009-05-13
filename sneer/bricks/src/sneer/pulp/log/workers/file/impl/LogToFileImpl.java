package sneer.pulp.log.workers.file.impl;

import static sneer.commons.environments.Environments.my;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sneer.brickness.StoragePath;
import sneer.pulp.log.formatter.LogFormatter;
import sneer.pulp.log.workers.file.LogToFile;

class LogToFileImpl implements LogToFile {

	private static final String FILENAME = "sneer.log";
	private static final boolean WRITE_TO_THE_END = true;
	private final StoragePath _persistenceConfig = my(StoragePath.class);	
	
	private final LogFormatter _formatter = my(LogFormatter.class);
	
	private void log(String msg){
        FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(new File(_persistenceConfig.get(), FILENAME), WRITE_TO_THE_END);
			fileWriter.write(msg);
			fileWriter.flush();
		} catch (IOException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} finally{
			try { fileWriter.close(); } catch (Exception ignore) {}
		}
	}
	
	@Override public void log(String message, Object... messageInsets) {  log(_formatter.format(message, messageInsets)); }
	@Override public void log(Throwable throwable, String message, Object... messageInsets) { log(_formatter.format(throwable, message, messageInsets));}
	@Override public void log(Throwable throwable) { log(_formatter.format(throwable)); }
	@Override public void logShort(Exception e, String message, Object... messageInsets) { log(_formatter.formatShort(e, message, messageInsets));}
}
