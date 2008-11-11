package wheel.reactive.impl;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import wheel.lang.Omnivore;
import wheel.reactive.Register;

public class RegisterImpl<VO> implements Register<VO> {

	class MyOutput extends AbstractSignal<VO> implements Serializable {

		@Override
		public VO currentValue() {
			synchronized (RegisterImpl.this) {
				return _currentValue;
			}
		}

		private static final long serialVersionUID = 1L;
	}

	class MySetter implements Omnivore<VO>, Serializable { private static final long serialVersionUID = 1L;

		@Override
		public void consume(VO newValue) {
			if (isSameValue(newValue)) return;

			synchronized (RegisterImpl.this) {
				_currentValue = newValue;
			
				output().notifyReceivers(newValue);
			}
		}
		
	}

	private volatile VO _currentValue;
	private final Omnivore<VO> _setter = new MySetter();
	private WeakReference<AbstractSignal<VO>> _output;
	
	public RegisterImpl(VO initialValue) {
		if (initialValue == null) return;
		setter().consume(initialValue);
	}


	public boolean isSameValue(VO value) {
		if (value == _currentValue) return true; 
		if (value != null && value.equals(_currentValue)) return true;
		return false;
	}


	public synchronized AbstractSignal<VO> output() {
		if (_output == null)
			_output = new WeakReference<AbstractSignal<VO>>(new MyOutput());
		else if	(_output.get() == null) {
			_output = new WeakReference<AbstractSignal<VO>>(new MyOutput());
		}
		
		return _output.get();
	}


	public Omnivore<VO> setter() {
		return _setter;
	}

	private static final long serialVersionUID = 1L;
}