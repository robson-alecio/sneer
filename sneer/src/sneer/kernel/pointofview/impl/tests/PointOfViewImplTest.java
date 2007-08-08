package sneer.kernel.pointofview.impl.tests;

import java.util.HashMap;
import java.util.Map;

import sneer.kernel.pointofview.tests.PartySimulator;
import sneer.kernel.pointofview.tests.PointOfViewTest;
import sneer.kernel.pointofview.tests.SovereignNetworkSimulator;
import wheel.io.network.mocks.OldNetworkMock;

public class PointOfViewImplTest extends PointOfViewTest {

	@Override
	protected SovereignNetworkSimulator prepareSubject() {
		return new SovereignNetworkSimulator() {

			private final OldNetworkMock _network = new OldNetworkMock();
			private int _nextPort = 8080;
			private Map<PartySimulator, Integer> _portsByPartySimulator = new HashMap<PartySimulator, Integer>();

			@Override
			public PartySimulator createPartySimulator(String name) {
				int port = _nextPort++;
				PartySimulatorImpl result = new PartySimulatorImpl(name, _network, port);
				_portsByPartySimulator .put(result, port);
				return result;
			}

			@Override
			public void connect(PartySimulator a, PartySimulator b) {
				PartySimulatorImpl implA = (PartySimulatorImpl)a;
				final PartySimulatorImpl implB = (PartySimulatorImpl)b;
				implA.connectTo("localhost", _portsByPartySimulator.get(b), implB.name());
				
				long t0 = System.currentTimeMillis();
				while (implB.contact("A") == null)
					if (System.currentTimeMillis() - t0 > 1000)
						throw new RuntimeException("Timeout");
			}
		};

	}

}
