package wheel.io.ui.impl.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import junit.framework.TestCase;
import wheel.io.ui.impl.ListSignalModel;
import wheel.reactive.Receiver;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.impl.ListElementAdded;

public class ListSignalModelTests extends TestCase implements ListSignal<String> {

	private Receiver<ListValueChange> _receiver;
	private List<String> _names = new ArrayList<String>();
	
	
	//Fix: use a ListSource in this test
	public void testAddition() {
		ListSignalModel _subject =new ListSignalModel(this);
		
		LogListDataListener probe = new LogListDataListener();
		_subject.addListDataListener(probe);
		
		_names.add("Banana");
		_receiver.receive(new ListElementAdded(0));
		
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

	public void addListReceiver(Receiver<ListValueChange> receiver) {
		_receiver = receiver;
	}

	public List<String> currentValue() {
		return _names;
	}

	@Override
	public String currentGet(int index) {
		return _names.get(index);
	}

	@Override
	public int currentSize() {
		return _names.size();
	}

	public Iterator<String> iterator() {
		throw new UnsupportedOperationException();
	}
	
}
