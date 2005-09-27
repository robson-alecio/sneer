package sneer.remote;



import java.io.IOException;

import sneer.life.Life;
import wheel.experiments.environment.network.ObjectSocket;
import wheelexperiments.reactive.Signal;
import wheelexperiments.reactive.Signals;
import wheelexperiments.reactive.Source;
import wheelexperiments.reactive.SourceImpl;
import wheelexperiments.reactive.Signal.Receiver;

abstract class IndianForObject<T> extends AbstractIndian {

	transient Signal<T> _observedSignal;
	transient final private Source<T> _sourceToNotify = createLocalSourceToNotify(); 
	
	public void reportAbout(Life life, ObjectSocket socket) {
		System.out.println("Sitting Bull reporting, sir.");
		_socket = socket;
		_observedSignal = signalToObserveOn(life);
		Signals.transientReception(_observedSignal, new Receiver<T>() {
			public void receive(T newValue) {
				try {
					System.out.println("Sending smoke signal..." + newValue);
					_socket.writeObject(new ObjectSmokeSignal(_id, newValue));
				} catch (IOException e) {
					e.printStackTrace();
					_observedSignal.removeReceiver(this);
				}
			}
		});
		
	}

	Source<T> localSourceToNotify() {
		return _sourceToNotify;
	}
	
	abstract protected Signal<T> signalToObserveOn(Life life);
	
	protected Source<T> createLocalSourceToNotify() {
		return new SourceImpl<T>();
	}

	@SuppressWarnings("unchecked")
	public void receive(SmokeSignal smokeSignal) {
		ObjectSmokeSignal objectSmokeSignal = (ObjectSmokeSignal) smokeSignal;
		_sourceToNotify.supply((T)objectSmokeSignal.newValue());
	}

	private static final long serialVersionUID = 1L;

}
