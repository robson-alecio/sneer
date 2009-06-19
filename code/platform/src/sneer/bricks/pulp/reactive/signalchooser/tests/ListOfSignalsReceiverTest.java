package sneer.bricks.pulp.reactive.signalchooser.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Assert;
import org.junit.Test;

import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.collections.CollectionSignals;
import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.pulp.reactive.collections.impl.VisitingListReceiver;
import sneer.bricks.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.bricks.pulp.reactive.signalchooser.SignalChooser;
import sneer.bricks.pulp.reactive.signalchooser.SignalChoosers;
import sneer.foundation.brickness.testsupport.BrickTest;

public class ListOfSignalsReceiverTest extends BrickTest {

	private final SignalChoosers _factory = my(SignalChoosers.class); 
	
	@SuppressWarnings("unused")
	private Object _refToAvoidGc;
	private ListRegister<Register<String>> _listRegister = my(CollectionSignals.class).newListRegister();
	private EventRecorder _recorder;

	@Test
	public void test() {
		_recorder = new EventRecorder();
		
		addElement("0");
		assertEvents("");
		_refToAvoidGc = _factory.receive(_listRegister.output(), newListOfSignalsReceiver(_listRegister.output()));
		
		Register<String> r1 = addElement("1");
		Register<String> r2 = addElement("2");
		addElement("3");
		addElement("4");
		assertEvents("Added=1, Added=2, Added=3, Added=4, ");

		_listRegister.move(1, 4);
		assertEvents("Moved=1->4, ");

		r1.setter().consume("1b");
		r2.setter().consume("2b");
		assertEvents("Changed=1b, Changed=2b, ");

		_listRegister.move(4, 1);
		assertEvents("Moved=4->1, ");

		_listRegister.removeAt(0);
		_listRegister.removeAt(3);
		_listRegister.removeAt(2);
		assertEvents("Removed=0, Removed=4, Removed=3, ");

		r1.setter().consume("1c");
		r2.setter().consume("2c");
		assertEvents("Changed=1c, Changed=2c, ");

		addElement("2c");
		assertEvents("Added=2c, ");

		_listRegister.move(1, 0);
		_listRegister.move(2, 3);
		assertEvents("Moved=1->0, Moved=2->3, ");

		_listRegister.remove(r1);
		assertEvents("Removed=1c, ");
	}

	private Register<String> addElement(String value) {
		return addElement(my(Signals.class).newRegister(value));
	}

	private Register<String> addElement(Register<String> register) {
		_listRegister.add(register);
		return register;
	}

	private void assertEvents(String expected) {
		Assert.assertEquals(expected, _recorder._events.toString());
		_recorder.clear();
	}
	
	private ListOfSignalsReceiver<Register<String>>newListOfSignalsReceiver(ListSignal<Register<String>> input) {
		return new MyListOfSignalsReceiver(input);
	}
	
	class MyListOfSignalsReceiver extends VisitingListReceiver<Register<String>> implements ListOfSignalsReceiver<Register<String>>{
		
		public MyListOfSignalsReceiver(ListSignal<Register<String>> input) {
			super(input);
		}

		@Override 
		public SignalChooser<Register<String>> signalChooser() {
			return new SignalChooser<Register<String>>(){ @Override public Signal<?>[] signalsToReceiveFrom( Register<String> element) {
				return new Signal<?>[]{element.output()};
			}};
		}

		@Override public void elementSignalChanged(Register<String> element) {  
			_recorder.record("Changed", value(element));}
		
		@Override public void elementAdded(int index, Register<String> element) { 
			_recorder.record("Added", value(element)); }
		
		@Override public void elementRemoved(int index, Register<String> element) { 
			_recorder.record("Removed", value(element)); }
		
		@Override public void elementReplaced(int index, Register<String> oldElement,	Register<String> newElement) { 
			_recorder.record("Replaced", value(oldElement), value(newElement)); }		

		@Override public void elementMoved(int index, int newIndex, Register<String> newElement) {
			_recorder.record("Moved", ""+index, ""+newIndex); }	
		
		private String value(Register<String> element) {
			return element.output().currentValue();
		}

	}
	
	private class EventRecorder {
		
		private StringBuilder _events = new StringBuilder();
		
		private void record(String name, String oldValue, String newValue) {
			record(name, oldValue + "->" + newValue);
		}

		private void record(String name, String value) {
			_events.append(name).append("=").append(value).append(", ");
		}

		private void clear() {
			_events = new StringBuilder();
		}
	}
}