package sneer.kernel.pointofview.tests;

import static sneer.tests.SneerTestDashboard.newTestsShouldRun;
import wheel.reactive.tests.LoopbackTester;
import wheel.testutil.TestOfInterface;

public abstract class PointOfViewTests extends TestOfInterface<SovereignNetworkSimulator> {

	public void testRemoteNameChange() {
		if (!newTestsShouldRun()) return;

		PartySimulator a = _subject.createPartySimulator("A");
		PartySimulator b = _subject.createPartySimulator("B");
		_subject.connect(a, b);

		LoopbackTester loopback = new LoopbackTester(a.contact("B").party().name(), b.nameSetter());
		loopback.testWithString();
	}

}
