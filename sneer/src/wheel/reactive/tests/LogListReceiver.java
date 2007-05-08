package wheel.reactive.tests;

import java.util.List;

import sneer.kernel.business.Contact;

import wheel.reactive.Receiver;
import wheel.reactive.list.ListSignal;
import wheel.reactive.list.ListValueChangeVisitorAdapter;
import wheel.reactive.list.ListSignal.ListValueChange;

public final class LogListReceiver<VO> implements Receiver<ListValueChange> {

	private StringBuilder _log = new StringBuilder();
	protected List<?> _list;
	private final ListSignal<VO> _input;
	
	public LogListReceiver(ListSignal<VO> input) {
		_input = input;
		_input.addListReceiver(this);
	}

	public synchronized void receive(ListValueChange valueChange) {
		updateListFieldOnListReplaced(valueChange);
		_log.append(valueChange.toString() +  ", list is: " + _list +"\n");		
	}

	private void updateListFieldOnListReplaced(ListValueChange valueChange) {
		valueChange.accept(new ListValueChangeVisitorAdapter(){
			@Override
			public void listReplaced() {
				_list = _input.currentValue();
			}});
	}
	
	public synchronized String log(){
		return _log.toString();
	}

}
