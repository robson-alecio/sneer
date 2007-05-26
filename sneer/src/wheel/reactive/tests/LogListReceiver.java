package wheel.reactive.tests;

import java.util.List;

import sneer.kernel.business.contacts.Contact;

import wheel.reactive.Receiver;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;

public final class LogListReceiver<VO> implements Receiver<ListValueChange> {

	private StringBuilder _log = new StringBuilder();
	private final ListSignal<VO> _input;
	
	public LogListReceiver(ListSignal<VO> input) {
		_input = input;
		_input.addListReceiver(this);
	}

	public synchronized void receive(ListValueChange valueChange) {
		_log.append(valueChange.toString() +  ", list is: ");
		for (int i = 0; i < _input.currentSize(); i++) {
			_log.append(_input.currentGet(i) + ", ");
		}
	}

	
	
	public synchronized String log(){
		return _log.toString();
	}

}
