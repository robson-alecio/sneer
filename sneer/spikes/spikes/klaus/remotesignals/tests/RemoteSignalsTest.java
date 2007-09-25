package spikes.klaus.remotesignals.tests;

import spikes.klaus.remotesignals.RemoteSignallingFacade;
import wheel.io.serialization.DeepCopyingPipe;
import wheel.testutil.TestOfInterface;

public class RemoteSignalsTest extends TestOfInterface<RemoteSignallingFacade> {

	@Override
	protected RemoteSignallingFacade prepareSubject() {
		DeepCopyingPipe pipe = new DeepCopyingPipe();
		return new RemoteSignallingFacade(pipe.input(), pipe.output());
	}

	public void testSignalRemoting() throws Exception {

		ArbitraryInterfaceImpl object = new ArbitraryInterfaceImpl();
		object._firstSource.setter().consume("Banana");

		_subject.sendSignals(object);
		ArbitraryInterface proxy = (ArbitraryInterface)_subject.getProxyForRemoteObject();
		
		assertEquals("Banana", proxy.signal1().currentValue());
	}


}
