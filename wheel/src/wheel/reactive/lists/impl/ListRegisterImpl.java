package wheel.reactive.lists.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.impl.AbstractNotifier;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListValueChange;

public class ListRegisterImpl<VO> implements ListRegister<VO> {
	

	private class MyOutput extends AbstractNotifier<ListValueChange> implements ListSignal<VO> {

		private static final long serialVersionUID = 1L;

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
		public void removeListReceiver(Object receiver) {
			removeReceiver(receiver);		
		}

		@Override
		protected void initReceiver(Omnivore<ListValueChange> receiver) {}

		@Override
		protected void notifyReceivers(ListValueChange valueChange) {
			super.notifyReceivers(valueChange);
		}

		public Iterator<VO> iterator() {
			return new ArrayList<VO>(_list).iterator(); //Optimize
		}

		@Override
		public Signal<Integer> size() {
			throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
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
	
	public VO get(int index){
		synchronized (_list){
			return _list.get(index);
		}
	}
	
	public boolean remove(VO element) {
		synchronized (_list) {
			int index = _list.indexOf(element);
			if (index == -1) return false;
			
			remove(index);

			return true;
		}
	}

	@Override
	public void remove(int index) {
		synchronized (_list) {
			_output.notifyReceivers(new ListElementToBeRemoved(index));
			_list.remove(index);
			_output.notifyReceivers(new ListElementRemoved(index));
		}
	}

	public ListSignal<VO> output() {
		return _output;
	}

	@Override
	public Omnivore<VO> adder() {
		return new Omnivore<VO>() { @Override public void consume(VO valueObject) {
			add(valueObject);
		}};
	}

	@Override
	public void replace(int index, VO newElement) {
		synchronized (_list) {
			_output.notifyReceivers(new ListElementToBeReplaced(index));
			_list.remove(index);
			_list.add(index, newElement);
			_output.notifyReceivers(new ListElementReplaced(index));
		}
	}


	private static final long serialVersionUID = 1L;
}
