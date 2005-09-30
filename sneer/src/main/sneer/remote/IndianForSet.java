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
	private SetSource<T> _setSourceToNotify = createLocalSetSourceToNotify();

	public SetSource<T> localSetSourceToNotify() {
		return _setSourceToNotify;
	}

	public void reportAbout(LifeView life, ObjectSocket socket) {
		System.out.println("Dances With Wolves reporting, sir.");
		_socket = socket;
		_observedSignal = setSignalToObserveOn(life);
		_observedSignal.addTransientSetReceiver(new Receiver<SetValueChange<T>>() {
			public void receive(SetValueChange<T> valueChange) {
				try {
					System.out.println("Sending smoke signal for set change..." + valueChange);
					_socket.writeObject(new SetSmokeSignal(_id, valueChange));
				} catch (IOException e) {
					_observedSignal.removeTransientSetReceiver(this);
				}
			}
		});
		
	}



	private static final long serialVersionUID = 1L;

	abstract protected SetSignal<T> setSignalToObserveOn(LifeView life);
	
	protected SetSource<T> createLocalSetSourceToNotify() {
		return new SetSource<T>();
	}

	@SuppressWarnings("unchecked")
	public void receive(SmokeSignal smokeSignal) {
		SetSmokeSignal setSmokeSignal = (SetSmokeSignal) smokeSignal;
		_setSourceToNotify.change((SetValueChange<T>)setSmokeSignal._change);
	}
}
