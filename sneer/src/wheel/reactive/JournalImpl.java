package wheel.reactive;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import wheel.lang.exceptions.Catcher;


public class JournalImpl<VO> implements Journal<VO> {

	public JournalImpl(Catcher notificationExceptionCatcher) {
		_notificationExceptionCatcher = notificationExceptionCatcher;
	}
	
	private final Set<Receiver<VO>> _receivers = new HashSet<Receiver<VO>>();
	private final List<VO> _contents = new LinkedList<VO>();
	private final Catcher _notificationExceptionCatcher;

	public synchronized void addReceiver(Receiver<VO> receiver) {
		_receivers.add(receiver);
		initReceiver(receiver);
	}

	private void initReceiver(Receiver<VO> receiver) {
		for (VO entry : _contents) notify(receiver, entry);
	}

	public synchronized void add(VO element) {
		_contents.add(element);
		notifyReceivers(element);
	}

	private void notifyReceivers(VO element) {
		for (Receiver<VO> receiver : _receivers) notify(receiver, element);
	}

	private void notify(Receiver<VO> receiver, VO elementAdded) {
		try {
			receiver.elementAdded(_contents.size() - 1);
		} catch (Throwable t) {
			_notificationExceptionCatcher.catchThis(t);
		}
	}
}
