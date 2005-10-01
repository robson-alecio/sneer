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
	transient private Source<T> _localSourceToNotify; 
	
	public void reportAbout(LifeView life, ObjectSocket socket) {
		_socket = socket;
		_observedSignal = signalToObserveOn(life);
		_observedSignal.addTransientReceiver(new Receiver<T>() {
			public void receive(T newValue) {
				try {
					_socket.writeObject(new ObjectSmokeSignal(_id, newValue));
				} catch (IOException e) {
					_observedSignal.removeReceiver(this);
				}
			}
		});
		
	}

	Source<T> localSourceToNotify() {
		if (_localSourceToNotify == null) _localSourceToNotify = new SourceImpl<T>();
		return _localSourceToNotify;
	}
	
	abstract protected Signal<T> signalToObserveOn(LifeView life);

	@SuppressWarnings("unchecked")
	public void receive(SmokeSignal smokeSignal) {
		ObjectSmokeSignal objectSmokeSignal = (ObjectSmokeSignal) smokeSignal;
		T newValue = (T)objectSmokeSignal.newValue();
		if (sameValue(newValue)) return;
		localSourceToNotify().supply(newValue);
	}

	private boolean sameValue(T newValue) {
		T currentValue = localSourceToNotify().currentValue();
		if (currentValue == newValue) return true;
		if (currentValue != null && currentValue.equals(newValue)) return true;
		return false;
	}

	private static final long serialVersionUID = 1L;

}
