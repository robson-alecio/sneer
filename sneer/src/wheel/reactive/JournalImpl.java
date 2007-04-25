package wheel.reactive;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import wheel.lang.exceptions.Catcher;
import wheel.reactive.ListSignal.ListValueChange;


public class JournalImpl<VO> implements Journal<VO> {

	public JournalImpl(Catcher notificationExceptionCatcher) {
		_notificationExceptionCatcher = notificationExceptionCatcher;
	}
	
	private final Set<Receiver<ListValueChange<VO>>> _receivers = new HashSet<Receiver<ListValueChange<VO>>>();
	private final List<VO> _contents = new LinkedList<VO>();
	private final Catcher _notificationExceptionCatcher;

	private void initReceiver(Receiver<ListValueChange<VO>> receiver) {
		receiver.receive(new ListReplaced<VO>(_contents));
	}

	public synchronized void add(VO element) {
		_contents.add(element);
		notifyReceivers();
	}

	private void notifyReceivers() {
		for (Receiver<ListValueChange<VO>> receiver : _receivers) notify(receiver);
	}

	private void notify(Receiver<ListValueChange<VO>> receiver) {
		try {
			receiver.receive(new ListElementAdded<VO>(_contents.size() - 1));
		} catch (Throwable t) {
			_notificationExceptionCatcher.catchThis(t);
		}
	}

	public void addListReceiver(
			Receiver<wheel.reactive.ListSignal.ListValueChange<VO>> receiver) {
		_receivers.add(receiver);
		initReceiver(receiver);
		
	}

	public List<VO> currentValue() {
		return Collections.unmodifiableList(_contents);		
	}
	
	
}
