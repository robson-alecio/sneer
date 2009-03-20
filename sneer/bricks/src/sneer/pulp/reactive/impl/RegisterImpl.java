package sneer.pulp.reactive.impl;

import java.lang.ref.WeakReference;

import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

public class RegisterImpl<VO> implements Register<VO> {

	class MyOutput extends AbstractSignal<VO> {

		@Override
		public VO currentValue() {
			return _currentValue;
		}

	}

	class MySetter implements Consumer<VO> {

		@Override
		public void consume(VO newValue) {
			if (isSameValue(newValue)) return;

			_currentValue = newValue;
			
			if (_output == null) return;
			((AbstractSignal<VO>)output()).notifyReceivers(newValue);
		}
		
	}

	private VO _currentValue;
	private WeakReference<AbstractSignal<VO>> _output;
	
	public RegisterImpl(VO initialValue) {
		if (initialValue == null) return;
		setter().consume(initialValue);
	}


	private boolean isSameValue(VO value) {
		if (value == _currentValue) return true; 
		if (value != null && value.equals(_currentValue)) return true;
		return false;
	}


	public synchronized Signal<VO> output() {
		if (_output == null)
			_output = new WeakReference<AbstractSignal<VO>>(new MyOutput());
		else if	(_output.get() == null) {
			_output = new WeakReference<AbstractSignal<VO>>(new MyOutput());
		}
		
		return _output.get();
	}


	public Consumer<VO> setter() {
		return new MySetter();
	}

}