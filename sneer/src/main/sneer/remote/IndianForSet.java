package sneer.remote;

import java.io.IOException;

import sneer.life.LifeView;
import wheel.experiments.environment.network.ObjectSocket;
import wheelexperiments.reactive.Receiver;
import wheelexperiments.reactive.SetSignal;
import wheelexperiments.reactive.SetSource;
import wheelexperiments.reactive.SetSignal.SetValueChange;

abstract class IndianForSet<T> extends AbstractIndian {

	private SetSignal<T> _observedSignal;
	private transient SetSource<T> _localSetSourceToNotify;

	SetSource<T> localSetSourceToNotify() {
		if (_localSetSourceToNotify == null) _localSetSourceToNotify = new SetSource<T>();
		return _localSetSourceToNotify;
	}

	public void reportAbout(LifeView life, ObjectSocket socket) {
		_socket = socket;
		_observedSignal = setSignalToObserveOn(life);
		_observedSignal.addTransientSetReceiver(new Receiver<SetValueChange<T>>() {
			public void receive(SetValueChange<T> valueChange) {
				try {
					_socket.writeObject(new SetSmokeSignal(_id, valueChange));
				} catch (IOException e) {
					_observedSignal.removeTransientSetReceiver(this);
				}
			}
		});
		
	}

	abstract protected SetSignal<T> setSignalToObserveOn(LifeView life);

	@SuppressWarnings("unchecked")
	public void receive(SmokeSignal smokeSignal) {
		SetSmokeSignal setSmokeSignal = (SetSmokeSignal) smokeSignal;
		_localSetSourceToNotify.change((SetValueChange<T>)setSmokeSignal._change);
	}

	private static final long serialVersionUID = 1L;

}

