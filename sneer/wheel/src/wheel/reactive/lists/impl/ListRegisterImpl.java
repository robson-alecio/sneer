package wheel.reactive.lists.impl;

import static sneer.commons.environments.Environments.my;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventNotifiers;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.impl.RegisterImpl;
import wheel.lang.Consumer;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListChange;

public class ListRegisterImpl<VO> implements ListRegister<VO> {
	
	private class MyOutput implements ListSignal<VO> {

		EventNotifier<ListChange<VO>> _notifier = my(EventNotifiers.class).create(new Consumer<Consumer<? super ListChange<VO>>>(){@Override public void consume(Consumer<? super ListChange<VO>> receiver) {
			//TODO
		}});

		@Override
		public VO currentGet(int index) {
			return _list.get(index);
		}

		@Override
		public int currentIndexOf(VO element) {
			return _list.indexOf(element);
		}

		@Override
		public int currentSize() {
			return _list.size();
		}

		@Override
		public void addReceiver(Consumer<? super ListChange<VO>> receiver) {
			_notifier.output().addReceiver(receiver);	
		}
		
		@Override
		public void removeReceiver(Object receiver) {
			_notifier.output().removeReceiver(receiver);		
		}

		void notifyReceivers(ListChange<VO> valueChange) {
			_notifier.notifyReceivers(valueChange);
		}

		public Iterator<VO> iterator() {
			synchronized (_list) {
				return new ArrayList<VO>(_list).iterator(); //Optimize
			}
		}

		@Override
		public Signal<Integer> size() {
			return _size.output();
		}

		@Override
		public List<VO> currentElements() {
			synchronized (_list) {
				return new ArrayList<VO>(_list);
			}
		}

	}

	
	protected final List<VO> _list = new ArrayList<VO>();
	protected final Register<Integer> _size = new RegisterImpl<Integer>(0);
	private MyOutput _output = new MyOutput();
	

	@Override
	public void add(VO element) {
		synchronized (_list) {
			_list.add(element);
			_size.setter().consume(_list.size());
		}
		_output.notifyReceivers(new ListElementAdded<VO>(_list.size() - 1, element));
	}
	
	@Override
	public void addAt(int index, VO element) {
		synchronized (_list) {
			_list.add(index, element);
			_size.setter().consume(_list.size());
		}
		_output.notifyReceivers(new ListElementAdded<VO>(index, element));
	}
	
	@Override
	public void remove(VO element) {
		synchronized (_list) {
			int index = _list.indexOf(element);
			if (index == -1) throw new IllegalArgumentException("ListRegister did not contain element to be removed: " + element);
			
			removeAt(index);
		}
	}

	@Override
	public void removeAt(int index) {
		VO oldValue;
		synchronized (_list) {
			oldValue = _list.remove(index);
			_size.setter().consume(_list.size());
		}
		_output.notifyReceivers(new ListElementRemoved<VO>(index, oldValue));
	}

	public ListSignal<VO> output() {
		return _output;
	}

	@Override
	public Consumer<VO> adder() {
		return new Consumer<VO>() { @Override public void consume(VO valueObject) {
			add(valueObject);
		}};
	}

	@Override
	public void replace(int index, VO newElement) {
		VO old;
		synchronized (_list) {
			old = _list.remove(index);
			_list.add(index, newElement);
		}
		_output.notifyReceivers(new ListElementReplaced<VO>(index, old, newElement));
	}
	
	@Override
	public void move(int oldIndex, int newIndex) {
		if(oldIndex==newIndex) return;
		int tmpIndex = newIndex>oldIndex ? newIndex-1 : newIndex;
		tmpIndex = newIndex<0 ? 0 : newIndex;
		
		VO element;
		synchronized (_list) {
			element = _list.get(oldIndex);
			_list.remove(oldIndex);
			if(newIndex > _list.size())
				_list.add(element);
			else
				_list.add(tmpIndex, element);
		}
		_output.notifyReceivers(new ListElementMoved<VO>(oldIndex, newIndex, element));
	}
	
	private static final long serialVersionUID = 1L;
}
