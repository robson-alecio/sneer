package org.friends.ui.tests;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import junit.framework.TestCase;

import org.friends.ui.ListSignalModel;

import wheel.reactive.ListElementAdded;
import wheel.reactive.ListReplaced;
import wheel.reactive.ListSignal;
import wheel.reactive.Receiver;
import wheel.reactive.ListSignal.ListValueChange;

public class ListModelTests extends TestCase implements ListSignal<String> {

	private Receiver<ListValueChange<String>> _receiver;
	private List<String> _names = new ArrayList<String>();
	
	public void testAddition() {
		ListSignalModel _subject =new ListSignalModel(this);
		
		LogListDataListener probe = new LogListDataListener();
		_subject.addListDataListener(probe);
		
		_receiver.receive(new ListReplaced<String>(_names));
		
		_names.add("Banana");
		_receiver.receive(new ListElementAdded<String>(0));
		
		assertEquals("added at 0", probe.log());
		assertEquals("Banana", _subject.getElementAt(0));
	}

	private static class LogListDataListener implements ListDataListener {
		
			private String _log;

			public void intervalRemoved(ListDataEvent arg0) {
				// TODO Auto-generated method stub
		
			}
		
			public void intervalAdded(ListDataEvent arg0) {
				int index0 = arg0.getIndex0();
				if (index0 == arg0.getIndex1()){
					_log = "added at " + index0;
				}
		
			}
		
			public void contentsChanged(ListDataEvent list) {
				// TODO Auto-generated method stub
		
			}
		
			public String log(){
				return _log;
			}
	}

	public void addListReceiver(Receiver<wheel.reactive.ListSignal.ListValueChange<String>> receiver) {
		_receiver = receiver;
	}

	public List<String> currentValue() {
		return _names;
	}
	
}
