package sneer.old.remote;

import java.io.IOException;

import sneer.old.life.LifeView;
import wheel.io.network.ObjectSocket;
import wheel.reactive.Receiver;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.SourceImpl;


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
					_observedSignal.removeTransientReceiver(this);
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
		localSourceToNotify().setter().consume(newValue);
	}

	private boolean sameValue(T newValue) {
		T currentValue = localSourceToNotify().output().currentValue();
		if (currentValue == newValue) return true;
		if (currentValue != null && currentValue.equals(newValue)) return true;
		return false;
	}

	private static final long serialVersionUID = 1L;

}
