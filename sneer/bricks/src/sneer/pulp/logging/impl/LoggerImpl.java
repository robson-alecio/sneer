package sneer.pulp.logging.impl;

import static wheel.lang.Environments.my;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import sneer.pulp.distribution.filtering.TupleFilterManager;
import sneer.pulp.logging.LogWhiteListEntry;
import sneer.pulp.tuples.TupleSpace;
import wheel.io.logging.impl.WheelLoggerImpl;
import wheel.lang.Consumer;
import wheel.reactive.EventNotifier;
import wheel.reactive.EventSource;
import wheel.reactive.impl.EventNotifierImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

class LoggerImpl extends WheelLoggerImpl implements sneer.pulp.logging.Logger, Consumer<LogWhiteListEntry> {

	private final TupleSpace _tupleSpace = my(TupleSpace.class);
	private final TupleFilterManager _filter = my(TupleFilterManager.class); 	{
		_filter.block(LogWhiteListEntry.class);
	}
	
	private final ListRegisterImpl<String> _phrases = new ListRegisterImpl<String>();
	
	private final EventNotifier<String> _loggedMessages = new EventNotifierImpl<String>();	

	LoggerImpl() {
		_tupleSpace.addSubscription(LogWhiteListEntry.class, this);
		_tupleSpace.keep(LogWhiteListEntry.class);
	}
	
	@Override
	public ListSignal<String> phrases() {
		return _phrases.output();
	}

	@Override
	public void whiteListEntryAdder(String phrase) {
		_phrases.adder().consume(phrase);
	}

	@Override
	public void whiteListEntryRemover(String phrase) {
		_phrases.remove(phrase);
	}	
	
	@Override
	public void consume(LogWhiteListEntry value) {
		whiteListEntryAdder(value.phrase);
	}

	@Override
	public Consumer<String> whiteListEntry() {
		return new Consumer<String>() { @Override public void consume(String phrase) {
			publish(phrase);
		}};
	}

	private void publish(String phrase) {
		_tupleSpace.publish(new LogWhiteListEntry(phrase)); 
	}

	@Override
	public EventSource<String> loggedMessages() {
		return _loggedMessages.output();
	}

	@Override
	protected void flush() {
		//NOP			
	}
	
	@Override
	protected void logThrowable(Throwable throwable) {
	    final Writer result = new StringWriter();
	    final PrintWriter printWriter = new PrintWriter(result);
	    throwable.printStackTrace(printWriter);
		_loggedMessages.notifyReceivers(result.toString());
	}	
	
	@Override
	protected void logHeader(String entry) {
		_loggedMessages.notifyReceivers(header(entry));
	}

	@Override
	protected void logSeparator() {
		_loggedMessages.notifyReceivers("\n");
	}	

}
