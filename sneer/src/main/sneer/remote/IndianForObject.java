package sneer.remote;

import java.io.IOException;

import sneer.life.LifeView;
import wheel.experiments.environment.network.ObjectSocket;
import wheelexperiments.reactive.Receiver;
import wheelexperiments.reactive.Signal;
import wheelexperiments.reactive.Source;
import wheelexperiments.reactive.SourceImpl;


abstract class IndianForObject<T> extends AbstractIndian {

	transient Signal<T> _observedSignal;
	transient final private Source<T> _sourceToNotify = createLocalSourceToNotify(); 
	
	public void reportAbout(LifeView life, ObjectSocket socket) {
		System.out.println("Sitting Bull reporting, sir.");
		_socket = socket;
		_observedSignal = signalToObserveOn(life);
		_observedSignal.addTransientReceiver(new Receiver<T>() {
			public void receive(T newValue) {
				try {
					System.out.println("Sending smoke signal..." + newValue);
					_socket.writeObject(new ObjectSmokeSignal(_id, newValue));
				} catch (IOException e) {
					_observedSignal.removeReceiver(this);
				}
			}
		});
		
	}

	Source<T> localSourceToNotify() {
		return _sourceToNotify;
	}
	
	abstract protected Signal<T> signalToObserveOn(LifeView life);
	
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
