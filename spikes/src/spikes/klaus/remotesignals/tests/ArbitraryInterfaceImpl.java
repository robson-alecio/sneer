package spikes.klaus.remotesignals.tests;

import wheel.reactive.Signal;
import wheel.reactive.Register;
import wheel.reactive.impl.SourceImpl;

public class ArbitraryInterfaceImpl implements ArbitraryInterface {

	final Register<String> _firstSource = new SourceImpl<String>(null);

	public Signal<String> signal1() {
		return _firstSource.output();
	}
	
	public Signal<Integer> signal2() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	private static final long serialVersionUID = 1L;
	
}
