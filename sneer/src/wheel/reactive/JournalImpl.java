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
		for (int i = _contents.size() - 1; i >= 0; i--)
			notify(receiver, i);
	}

	public synchronized void add(VO element) {
		_contents.add(element);
		notifyReceivers(_contents.size() - 1);
	}

	private void notifyReceivers(int index) {
		for (Receiver<VO> receiver : _receivers) notify(receiver, index);
	}

	private void notify(Receiver<VO> receiver, int index) {
		try {
			receiver.elementAdded(index);
		} catch (Throwable t) {
			_notificationExceptionCatcher.catchThis(t);
		}
	}
}
