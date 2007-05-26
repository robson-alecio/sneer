package sneer.kernel.gui.contacts.tests;

import static junit.framework.Assert.*;

import sneer.kernel.business.contacts.Contact;
import wheel.reactive.Receiver;
import wheel.reactive.Signal;

public class Sentinel {

	private String _expectedValue;
	protected boolean _isExpecting;
	private final Signal<?> _signal;

	public <VO> Sentinel(Signal<VO> signal, String initialValue) {
		_signal = signal;
		assertEquals(initialValue, _signal.currentValue().toString());
		
		expect(initialValue);
		Receiver<VO> myReceiver = myReceiver();
		signal.addReceiver(myReceiver);
	}

	public void expect(String value) {
		assertSatisfied();
		_expectedValue = value;
		_isExpecting = true;
	}

	private <VO>Receiver<VO> myReceiver() {
		return new Receiver<VO>(){
			@Override
			public void receive(VO valueChange) {
				assertTrue(_isExpecting);
				_isExpecting = false;
				
				assertEquals(_expectedValue, valueChange.toString());

				assertEquals(_expectedValue, _signal.currentValue().toString());
			}};
	}

	public void assertSatisfied() {
		assertTrue("Previous expected value not received: " + _expectedValue, !_isExpecting);	
	}



}
