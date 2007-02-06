package wheel.reactive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wheel.lang.exceptions.Catcher;


public class JournalImpl implements Journal {

	public JournalImpl(Catcher notificationExceptionCatcher) {
		_notificationExceptionCatcher = notificationExceptionCatcher;
	}
	
	private final Set<Receiver> _receivers = new HashSet<Receiver>();
	private final List<Object> _contents = new ArrayList<Object>();
	private final Catcher _notificationExceptionCatcher;

	public synchronized void addReceiver(Receiver receiver) {
		_receivers.add(receiver);
		initReceiver(receiver);
	}

	private void initReceiver(Receiver receiver) {
		for (Object entry : _contents) notify(receiver, entry);
	}

	public synchronized void add(Object element) {
		_contents.add(element);
		notifyReceivers(element);
	}

	private void notifyReceivers(Object element) {
		for (Receiver receiver : _receivers) notify(receiver, element);
	}

	private void notify(Receiver receiver, Object elementAdded) {
		try {
			receiver.elementAdded(elementAdded);
		} catch (Throwable t) {
			_notificationExceptionCatcher.catchThis(t);
		}
	}
}
