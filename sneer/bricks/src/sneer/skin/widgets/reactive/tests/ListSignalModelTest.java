package sneer.skin.widgets.reactive.tests;

import static sneer.brickness.environments.Environments.my;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.junit.Assert;
import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class ListSignalModelTest extends BrickTest {

	private final ReactiveWidgetFactory _factory = my(ReactiveWidgetFactory.class); 
	
	private ListRegister<Register<String>> _listRegister = new ListRegisterImpl<Register<String>>();
	private StringBuilder _events;

	@Test
	public void test() {
		clearEvents();
		
		addElement("0");
		assertEvents("");
		
		ListModel subject = _factory.newListSignalModel(_listRegister.output(), chooser());
		subject.addListDataListener(eventRecorder());
		
		Register<String> r1 = addElement("1");
		Register<String> r2 = addElement("2");
		addElement("3");
		addElement("4");
		assertEvents("Added 1 1, Added 2 2, Added 3 3, Added 4 4, ");
		
		r1.setter().consume("1b");
		r2.setter().consume("2b");
		assertEvents("Changed 1 1, Changed 2 2, ");
		
		_listRegister.removeAt(4);
		_listRegister.removeAt(0);
		_listRegister.removeAt(1);
		assertEvents("Removed 4 4, Removed 0 0, Removed 1 1, ");
		
		r1.setter().consume("1c");
		r2.setter().consume("2c");
		assertEvents("Changed 0 0, ");
	}

	private ListDataListener eventRecorder() {
		return new ListDataListener(){
		
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
			}};
	}

	private SignalChooser<Register<String>> chooser() {
		return new SignalChooser<Register<String>>(){ @Override public Signal<?>[] signalsToReceiveFrom(Register<String> element) {
			return new Signal<?>[]{ element.output() };
		}};
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

	private void clearEvents() {
		_events = new StringBuilder();
	}
}