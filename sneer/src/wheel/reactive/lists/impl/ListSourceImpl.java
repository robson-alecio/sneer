package wheel.reactive.lists.impl;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import wheel.lang.Omnivore;
import wheel.reactive.impl.AbstractNotifier;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.ListValueChange;

public class ListSourceImpl<VO> implements ListSource<VO> {
	
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
		public void addListReceiver(Omnivore<ListValueChange> receiver) {
			addReceiver(receiver);		
		}

		@Override
		protected void initReceiver(Omnivore<ListValueChange> receiver) {}

		@Override
		protected void notifyReceivers(ListValueChange valueChange) {
			super.notifyReceivers(valueChange);
		}

		public Iterator<VO> iterator() {
			return _list.iterator();
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
	
	public boolean remove(VO element) {
		synchronized (_list){
			int index = _list.indexOf(element);
			if (index == -1) return false;
			
			_output.notifyReceivers(new ListElementToBeRemoved(index));
			_list.remove(index);
			_output.notifyReceivers(new ListElementRemoved(index));

			return true;
		}
	}

	public ListSignal<VO> output() {
		return _output;
	}

	@Override
	public Omnivore<VO> adder() {
		return new Omnivore<VO>(){
			@Override
			public void consume(VO valueObject) {
				add(valueObject);
			}
		};
	}

}
