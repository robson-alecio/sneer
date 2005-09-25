package wheelexperiments.reactive;

import wheelexperiments.reactive.Signal.Receiver;

public class Signals {

	static public <T> void transientReception(Signal<T> signal, Signal.Receiver<T> transientReceiver) {
		signal.addReceiver(new TransientReceiver<T>(signal, transientReceiver));
	}
	
	private static class TransientReceiver<T> implements Signal.Receiver<T> {
		
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
	}

}
