package wheel.reactive;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import wheel.lang.exceptions.Catcher;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.impl.ListElementAdded;
import wheel.reactive.lists.impl.ListReplaced;


public class JournalImpl<VO> implements Journal<VO> {

	public JournalImpl(Catcher notificationExceptionCatcher) {
		_notificationExceptionCatcher = notificationExceptionCatcher;
	}
	
	private final Set<Receiver<ListValueChange>> _receivers = new HashSet<Receiver<ListValueChange>>();
	private final List<VO> _contents = new LinkedList<VO>();
	private final Catcher _notificationExceptionCatcher;

	private void initReceiver(Receiver<ListValueChange> receiver) {
		receiver.receive(new ListReplaced(0, currentSize()));
	}

	public synchronized void add(VO element) {
		_contents.add(element);
		notifyReceivers();
	}

	private void notifyReceivers() {
		for (Receiver<ListValueChange> receiver : _receivers) notifyReceiver(receiver);
	}

	private void notifyReceiver(Receiver<ListValueChange> receiver) {
		try {
			receiver.receive(new ListElementAdded(_contents.size() - 1));
		} catch (Throwable t) {
			_notificationExceptionCatcher.catchThis(t);
		}
	}

	public void addListReceiver(
			Receiver<wheel.reactive.lists.ListValueChange> receiver) {
		_receivers.add(receiver);
		initReceiver(receiver);
		
	}

	@Override
	public VO currentGet(int index) {
		return _contents.get(index);
	}

	@Override
	public int currentSize() {
		return _contents.size();
	}
	
	
}
