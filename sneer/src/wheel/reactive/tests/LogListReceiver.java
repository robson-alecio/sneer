package wheel.reactive.tests;

import java.util.List;

import wheel.reactive.ListValueChangeVisitorAdapter;
import wheel.reactive.Receiver;
import wheel.reactive.ListSignal.ListValueChange;

public final class LogListReceiver<T> implements
		Receiver<ListValueChange<T>> {

	private StringBuilder _log = new StringBuilder();
	protected List<T> _list;
	
	@Override
	public synchronized void receive(ListValueChange<T> valueChange) {
		updateListFieldOnListReplaced(valueChange);
		_log.append(valueChange.toString() +  ", list is: " + _list +"\n");		
	}

	private void updateListFieldOnListReplaced(
			ListValueChange<T> valueChange) {
		valueChange.accept(new ListValueChangeVisitorAdapter<T>(){@Override
			public void listReplaced(List<T> newList) {
				_list = newList;
			}});
	}
	
	public synchronized String log(){
		return _log.toString();
	}

}
