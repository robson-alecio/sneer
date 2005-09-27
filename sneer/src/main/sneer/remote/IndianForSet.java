package sneer.remote;

import java.io.IOException;

import sneer.life.LifeView;
import wheel.experiments.environment.network.ObjectSocket;
import wheelexperiments.reactive.Signals;
import wheelexperiments.reactive.signals.SetSignal;
import wheelexperiments.reactive.signals.SetSource;
import wheelexperiments.reactive.signals.SetSignal.Receiver;

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
		Signals.transientReception(_observedSignal, new Receiver<T>() {

			public void elementAdded(T newElement) {
				try {
					System.out.println("Sending smoke signal for elementAdded..." + newElement);
					_socket.writeObject(new SetSmokeSignal(_id, newElement, null));
				} catch (IOException e) {
					e.printStackTrace();
					_observedSignal.removeReceiver(this);
				}
				
			}

			public void elementRemoved(T removedElement) {
				try {
					System.out.println("Sending smoke signal for elementAdded..." + removedElement);
					_socket.writeObject(new SetSmokeSignal(_id, null, removedElement));
				} catch (IOException e) {
					e.printStackTrace();
					_observedSignal.removeReceiver(this);
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
		if (setSmokeSignal.getElementAdded() != null) {
			_setSourceToNotify.add((T) setSmokeSignal.getElementAdded());
		}
		if (setSmokeSignal.getElementRemoved() != null) {
			_setSourceToNotify.remove((T) setSmokeSignal.getElementRemoved());
		}
	}
}
