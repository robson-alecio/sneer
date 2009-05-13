package sneer.pulp.log.workers.sysout.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.log.formatter.LogFormatter;
import sneer.pulp.log.workers.sysout.LogToSystemOut;

public class LogToSystemOutImpl implements LogToSystemOut {

	private final LogFormatter _formatter = my(LogFormatter.class);
	
	private void log(String msg){
		System.out.println(msg);
	}
	
	@Override  public void log(String message, Object... messageInsets) {  log(_formatter.format(message, messageInsets)); }
	@Override  public void log(Throwable throwable, String message, Object... messageInsets) { log(_formatter.format(throwable, message, messageInsets));}
	@Override public void log(Throwable throwable) { log(_formatter.format(throwable)); }
	@Override public void logShort(Exception e, String message, Object... messageInsets) { log(_formatter.formatShort(e, message, messageInsets));}
}
