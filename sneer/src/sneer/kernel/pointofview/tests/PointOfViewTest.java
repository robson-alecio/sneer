package sneer.kernel.pointofview.tests;

import wheel.reactive.tests.LoopbackTester;
import wheel.testutil.TestOfInterface;

public abstract class PointOfViewTest extends TestOfInterface<SovereignNetworkSimulator> {

	private PartySimulator _a;
	private PartySimulator _b;

	@Override
	protected void setUp() {
		super.setUp();
		
		_a = _subject.createPartySimulator("A");
		_b = _subject.createPartySimulator("B");
		_subject.connect(_a, _b);
	}
	
	public void testRemoteNameChange() {
		LoopbackTester loopback = new LoopbackTester(_a.contact("B").party().name(), _b.nameSetter());
		loopback.testWithStrings();
	}

}
