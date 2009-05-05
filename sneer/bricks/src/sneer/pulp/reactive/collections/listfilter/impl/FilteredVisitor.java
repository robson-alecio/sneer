package sneer.pulp.reactive.collections.listfilter.impl;

import static sneer.commons.environments.Environments.my;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.collections.ListChange;
import sneer.pulp.reactive.collections.ListRegister;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.ReactiveCollections;
import sneer.pulp.reactive.collections.listfilter.Filter;
import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.pulp.reactive.signalchooser.SignalChooserManager;
import sneer.pulp.reactive.signalchooser.SignalChooserManagerFactory;
import wheel.reactive.impl.ListSignalOwnerReference;
import wheel.reactive.lists.VisitorAdapter;

final class FilteredVisitor<T> extends VisitorAdapter<T> implements ListOfSignalsReceiver<T>{

	private final ListSignal<T> _input;
	private final Filter<T> _filter;	
	private final SignalChooser<T> _chooser;

	private final SignalChooserManagerFactory _signalChooserManagerFactory = my(SignalChooserManagerFactory.class);
	
	@SuppressWarnings("unused")
	private SignalChooserManager<T> _signalChooserManagerToAvoidGc;

	private final Object _monitor = new Object();
	
	private FilterSupport _filterSupport;
	
	FilteredVisitor(ListSignal<T> input, Filter<T> filter, SignalChooser<T> chooser) {
		_input = input;
		_filter = filter;
		_chooser = chooser;
		_signalChooserManagerToAvoidGc = _signalChooserManagerFactory.newManager(input, this);
		_filterSupport = new FilterSupport();
	}
	
	@Override public void elementAdded(int index, T element) { _filterSupport.add(element); }
	@Override public void elementInserted(int index, T element) { _filterSupport.add(element);}
	@Override public void elementRemoved(int index, T element) {_filterSupport.remove(element);}
	@Override public void elementReplaced(int index, T oldElement, T newElement) {_filterSupport.replace(oldElement, newElement);}
	@Override public void elementSignalChanged(int index, T element) { _filterSupport.signalChanged(element);}

	@Override
	public SignalChooser<T> signalChooser() {
		return _chooser;
	}

	ListSignal<T> output() {
		return new ListSignalOwnerReference<T>(_filterSupport._filteredList.output(), this);
	}
	
	private class FilterSupport{
		private final ListRegister<T> _filteredList = my(ReactiveCollections.class).newListRegister();
		
		private final Consumer<ListChange<T>> _receiverAvoidGc = new Consumer<ListChange<T>>(){@Override public void consume(ListChange<T> change) {
			change.accept(FilteredVisitor.this);
		}};
		
		private FilterSupport() {
			synchronized (_monitor) {
				initFilteredList();
				_input.addReceiver(_receiverAvoidGc);
			}
		}		
		
		public void signalChanged(T element) {
			synchronized (_monitor) {
				if(_filter.select(element)) 
					_filteredList.add(element);
				else 
					_filteredList.remove(element);
			}			
		}

		private void initFilteredList() {
			for (T element : _input)  
				add(element);		
		}

		public void add(T element) {
			synchronized (_monitor) {
				if(_filter.select(element))
					_filteredList.add(element);
			}
		}

		public void replace(T oldElement, T newElement) {
			synchronized (_monitor) {
				_filteredList.remove(oldElement);
				if(_filter.select(newElement))	_filteredList.add(newElement);
			}
		}

		public void remove(T element) {
			synchronized (_monitor) {
				_filteredList.remove(element);
			}
		}
	}
}