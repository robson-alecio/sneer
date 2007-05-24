package sneer.kernel.gui.contacts.tests;

import wheel.reactive.Receiver;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.SourceImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;

public class ListPrinter {

	private final ListSignal<?> _list;
	protected Source<String> _output;

	public ListPrinter(ListSignal<?> list) {
		_list = list;
		_output = new SourceImpl<String>(_list.toString());
		_list.addListReceiver(myReceiver());
	}

	private Receiver<ListValueChange> myReceiver() {
		return new Receiver<ListValueChange>() {
			@Override
			public void receive(ListValueChange valueChange) {
				//Optimize
				String listToString = listToString();
				if (!_output.output().currentValue().equals(listToString))
					_output.setter().consume(listToString);
			}

			private String listToString() {
				if (_list.currentSize() == 0) return "";
				
				StringBuilder builder = new StringBuilder();
				builder.append(_list.currentGet(0));
				for (int i = 1; i < _list.currentSize(); i++) {
					builder.append("," + _list.currentGet(i));					
				}
				
				return builder.toString();
			}
		};
	}

	public Signal<String> output() {
		return _output.output();
	}

}
