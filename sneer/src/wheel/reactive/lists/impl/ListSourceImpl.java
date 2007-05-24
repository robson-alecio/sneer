package wheel.reactive.lists.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wheel.reactive.AbstractNotifier;
import wheel.reactive.Receiver;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.ListValueChange;

//Fix: make all methods synchronized
public class ListSourceImpl<VO> implements ListSource<VO>, Serializable {

	private class MyOutput extends AbstractNotifier<ListValueChange> implements ListSignal<VO> {

		@Override
		public VO currentGet(int index) {
			return _list.get(index);
		}

		@Override
		public int currentSize() {
			return _list.size();
		}

		@Override
		public void addListReceiver(Receiver<ListValueChange> receiver) {
			addReceiver(receiver);		
		}

		@Override
		protected void initReceiver(Receiver<ListValueChange> receiver) {
			receiver.receive(ListReplaced.SINGLETON);
		}

		@Override
		protected void notifyReceivers(ListValueChange valueChange) {
			super.notifyReceivers(valueChange);
		}

	}

	private final List<VO> _list = new ArrayList<VO>();
	private MyOutput _output = new MyOutput();
	
	public void add(VO element){
		synchronized (_list){
			_list.add(element);
			_output.notifyReceivers(new ListElementAdded(_list.size() - 1));
		}
	}
	
	private static final long serialVersionUID = 0L;

	public boolean remove(VO element) {
		synchronized (_list){
			int index = _list.indexOf(element);
			if (!_list.remove(element))
				return false;
			
			_output.notifyReceivers(new ListElementRemoved(index));
			return true;
		}
	}

	public ListSignal<VO> output() {
		return _output;
	}
	
	
}
