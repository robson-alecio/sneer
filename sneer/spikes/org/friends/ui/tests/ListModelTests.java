package org.friends.ui.tests;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.friends.ui.ListModel;

import junit.framework.TestCase;

public class ListModelTests extends TestCase {


	public void testAddition() {
		ListModel<String> _subject =new ListModel<String>();
		LogListDataListener logListDataListener = new LogListDataListener();
		_subject.addListDataListener(logListDataListener);
		
		List<String> names = new ArrayList<String>();
		_subject.listReplaced(names);
		
		names.add("Banana");
		_subject.elementAdded(0);
		
		assertEquals("added at 0", logListDataListener.log());
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
	
}
