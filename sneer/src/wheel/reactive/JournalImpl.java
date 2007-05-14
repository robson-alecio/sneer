package wheel.reactive;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import wheel.lang.exceptions.Catcher;
import wheel.reactive.list.ListElementAdded;
import wheel.reactive.list.ListReplaced;
import wheel.reactive.list.ListSignal.ListValueChange;


public class JournalImpl<VO> implements Journal<VO> {

	public JournalImpl(Catcher notificationExceptionCatcher) {
		_notificationExceptionCatcher = notificationExceptionCatcher;
	}
	
	private final Set<Receiver<ListValueChange>> _receivers = new HashSet<Receiver<ListValueChange>>();
	private final List<VO> _contents = new LinkedList<VO>();
	private final Catcher _notificationExceptionCatcher;

	private void initReceiver(Receiver<ListValueChange> receiver) {
		receiver.receive(ListReplaced.SINGLETON);
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
			Receiver<wheel.reactive.list.ListSignal.ListValueChange> receiver) {
		_receivers.add(receiver);
		initReceiver(receiver);
		
	}

	public List<VO> currentValue() {
		return Collections.unmodifiableList(_contents);		
	}

	@Override
	public VO get(int index) {
		return currentValue().get(index);
	}
	
	
}
