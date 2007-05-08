package wheel.reactive.tests;

import java.util.List;

import wheel.reactive.Receiver;
import wheel.reactive.list.ListValueChangeVisitorAdapter;
import wheel.reactive.list.ListSignal.ListValueChange;

public final class LogListReceiver<VO> implements
		Receiver<ListValueChange<VO>> {

	private StringBuilder _log = new StringBuilder();
	protected List<?> _list;
	
	public synchronized void receive(ListValueChange<VO> valueChange) {
		updateListFieldOnListReplaced(valueChange);
		_log.append(valueChange.toString() +  ", list is: " + _list +"\n");		
	}

	private void updateListFieldOnListReplaced(
			ListValueChange<VO> valueChange) {
		valueChange.accept(new ListValueChangeVisitorAdapter<VO>(){@Override
			public void listReplaced(List<VO> newList) {
				_list = newList;
			}});
	}
	
	public synchronized String log(){
		return _log.toString();
	}

}
