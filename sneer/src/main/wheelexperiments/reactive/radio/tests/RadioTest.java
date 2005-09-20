package wheelexperiments.reactive.radio.tests;

import wheel.experiments.environment.network.OldNetwork;
import wheel.experiments.environment.network.mocks.NetworkMock;
import wheelexperiments.reactive.Signal;
import wheelexperiments.reactive.Signal.Receiver;
import wheelexperiments.reactive.radio.RemoteSignal;
import wheelexperiments.reactive.radio.RemoteTransmitter;
import wheelexperiments.reactive.tests.ConnectionTest;

public class RadioTest extends ConnectionTest {

	private Receiver<Object> _remoteTransmitter;
	private Signal<Object> _remoteSignal;

	private OldNetwork _network;
	
    @Override
    protected void setUp() throws Exception {
    	_network = new NetworkMock();
    	
    	_remoteSignal = new RemoteSignal<Object>(_network);
    	_remoteTransmitter = new RemoteTransmitter<Object>(_network);
    }

	@Override
	protected Signal<Object> output() {
		return _remoteSignal;
	}

	@Override
	protected Receiver<Object> input() {
		return _remoteTransmitter;
	}

}
