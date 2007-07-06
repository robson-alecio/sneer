package sneer.kernel.gui.contacts.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class Sentinel {

	private String _expectedValue;
	protected boolean _isExpecting;
	private final Signal<?> _signal;

	public <VO> Sentinel(Signal<VO> signal, String initialValue) {
		_signal = signal;
		assertEquals(initialValue, _signal.currentValue().toString());
		
		expect(initialValue);
		Omnivore<VO> myReceiver = myReceiver();
		signal.addReceiver(myReceiver);
	}

	public void expect(String value) {
		assertSatisfied();
		_expectedValue = value;
		_isExpecting = true;
	}

	private <VO>Omnivore<VO> myReceiver() {
		return new Omnivore<VO>(){
			@Override
			public void consume(VO valueChange) {
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
