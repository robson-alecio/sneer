package sneer.remote;



import java.io.IOException;
import java.io.Serializable;

import sneer.life.Life;
import sneer.life.LifeView;
import wheel.experiments.environment.network.ObjectSocket;
import wheelexperiments.reactive.Signal;
import wheelexperiments.reactive.Signals;
import wheelexperiments.reactive.Source;
import wheelexperiments.reactive.Signal.Receiver;

abstract class Indian<T> implements Query<String>, Serializable {

	static private int _nextId = 1;

	private final int _id = _nextId++;

	transient Signal<T> _observedSignal;
	transient private ObjectSocket _socket;

	transient final private Source<T> _sourceToNotify = createLocalSourceToNotify(); 
	
	public void reportAbout(Life life, ObjectSocket socket) {
		System.out.println("Sitting Bull reporting, sir.");
		_socket = socket;
		_observedSignal = signalToObserveOn(life);
		Signals.transientReception(_observedSignal, new Receiver<T>() {
			public void receive(T newThoughtOfTheDay) {
				try {
					System.out.println("Sending smoke signal..." + newThoughtOfTheDay);
					_socket.writeObject(new SmokeSignal<String>(_id, newThoughtOfTheDay));
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
	abstract protected Source<T> createLocalSourceToNotify();

	public int id() {
		return _id;
	}

	public void receive(SmokeSignal<T> smokeSignal) {
		_sourceToNotify.supply(smokeSignal.newValue());
	}

	private static final long serialVersionUID = 1L;

	public String executeOn(LifeView ignored) {
		return "Ignored";
	}

}
