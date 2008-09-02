package wheel.io.ui.impl.tests;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import wheel.io.ui.impl.ListSignalModel;
import wheel.io.ui.impl.ListSignalModel.SignalChooser;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class ListSignalModelTest {

	private ListRegister<Register<String>> _listRegister;
	private ListSignalModel<Register<String>> _subject;
	private StringBuilder _events;

	@Before
	public void initRegister() {
		_listRegister = new ListRegisterImpl<Register<String>>();

		SignalChooser<Register<String>> signalChooser = new SignalChooser<Register<String>>(){ @Override public Signal<?>[] signalsToReceiveFrom(Register<String> element) {
			return new Signal<?>[]{ element.output() };
		}};
		_subject = new ListSignalModel<Register<String>>(_listRegister.output(), signalChooser);

		_subject.addListDataListener(new ListDataListener(){

			@Override
			public void contentsChanged(ListDataEvent arg0) {
				recordEvent(arg0);
			}

			@Override
			public void intervalAdded(ListDataEvent arg0) {
				recordEvent(arg0);
			}

			@Override
			public void intervalRemoved(ListDataEvent arg0) {
				recordEvent(arg0);
			}}
		);
		
		clearEvents();
	}

	private void clearEvents() {
		_events = new StringBuilder();
	}
	
	@Test
	public void test() {
		addElement("0");
		Register<String> r1 = addElement("1");
		Register<String> r2 = addElement("2");
		addElement("3");
		addElement("4");
		assertEvents("Changed 0 0, Added 0 0, Changed 1 1, Added 1 1, Changed 2 2, Added 2 2, Changed 3 3, Added 3 3, Changed 4 4, Added 4 4, ");
		
		r1.setter().consume("1b");
		r2.setter().consume("2b");
		assertEvents("Changed 1 1, Changed 2 2, ");
		
		_listRegister.remove(4);
		_listRegister.remove(0);
		_listRegister.remove(1);
		assertEvents("Removed 4 4, Removed 0 0, Removed 1 1, ");
		
		r1.setter().consume("1c");
		r2.setter().consume("2c");
		assertEvents("Changed 0 0, ");
	}

	private Register<String> addElement(String value) {
		RegisterImpl<String> result = new RegisterImpl<String>(value);
		_listRegister.add(result);
		return result;
	}

	private void assertEvents(String expected) {
		Assert.assertEquals(expected, _events.toString());
		clearEvents();
		
	}

	private void recordEvent(ListDataEvent event) {
		String result = type(event);
		result += " " + event.getIndex0();
		result += " " + event.getIndex0();
		_events.append(result + ", ");
	}

	private String type(ListDataEvent event) {
		int type = event.getType();
		if (type == ListDataEvent.CONTENTS_CHANGED) return "Changed";
		if (type == ListDataEvent.INTERVAL_ADDED) return "Added";
		if (type == ListDataEvent.INTERVAL_REMOVED) return "Removed";
		throw new IllegalStateException();
	}

}
