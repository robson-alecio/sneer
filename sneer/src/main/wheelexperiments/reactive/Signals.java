package wheelexperiments.reactive;

import java.io.Serializable;

import wheelexperiments.reactive.Signal.Receiver;
import wheelexperiments.reactive.signals.SetSignal;

public class Signals {

	static public <T> void transientReception(Signal<T> signal, Signal.Receiver<T> transientReceiver) {
		signal.addReceiver(new TransientReceiver<T>(signal, transientReceiver));
	}
	
	
	private static class TransientReceiver<T> implements Signal.Receiver<T>, Serializable {
		
		transient private final Receiver<T> _delegate;
		private final Signal<T> _signal;
	
		private TransientReceiver(Signal<T> signal, Signal.Receiver<T> delegate) {
			_signal = signal;
			_delegate = delegate;
		}
		
		public void receive(T newValue) {
			if (_delegate == null) {
				_signal.removeReceiver(this);
				return;
			}
			_delegate.receive(newValue);
		}

		private static final long serialVersionUID = 1L;
	}

	static public <T> void transientReception(SetSignal<T> setSignal, SetSignal.Receiver<T> transientReceiver) {
		setSignal.addReceiver(new TransientSetReceiver<T>(setSignal, transientReceiver));
	}

	private static class TransientSetReceiver<T> implements SetSignal.Receiver<T>, Serializable {
		
		transient private final SetSignal.Receiver<T> _delegate;
		private final SetSignal<T> _signal;
	
		private TransientSetReceiver(SetSignal<T> signal, SetSignal.Receiver<T> delegate) {
			_signal = signal;
			_delegate = delegate;
		}

		public void elementAdded(T newElement) {
			if (_delegate == null) {
				_signal.removeReceiver(this);
				return;
			}
			_delegate.elementAdded(newElement);
		}

		public void elementRemoved(T removedElement) {
			if (_delegate == null) {
				_signal.removeReceiver(this);
				return;
			}
			_delegate.elementRemoved(removedElement);
		}

		private static final long serialVersionUID = 1L;
	}

	
	
}
