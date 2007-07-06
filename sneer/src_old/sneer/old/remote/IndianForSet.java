package sneer.old.remote;

import java.io.IOException;
import java.util.Set;

import sneer.old.life.LifeView;
import wheel.io.network.ObjectSocket;
import wheel.lang.Omnivore;
import wheel.reactive.SetSignal;
import wheel.reactive.SetSource;
import wheel.reactive.SetSignal.SetValueChange;

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
		_observedSignal.addTransientSetReceiver(new Omnivore<SetValueChange<T>>() {
			public void consume(SetValueChange<T> valueChange) {
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
		SetValueChange<T> reportedChange = (SetValueChange<T>)setSmokeSignal._change;
		SetValueChange<T> delta = delta(reportedChange);
		if (delta == null) return;
		localSetSourceToNotify().change(delta);
	}

	private SetValueChange<T> delta(SetValueChange<T> reportedChange) {
		Set<T> current = localSetSourceToNotify().currentElements();
		reportedChange.elementsAdded().removeAll(current);
		reportedChange.elementsRemoved().retainAll(current);
		if (reportedChange.elementsAdded().isEmpty() && reportedChange.elementsRemoved().isEmpty())
			return null;
		return reportedChange;
	}

	private static final long serialVersionUID = 1L;

}

