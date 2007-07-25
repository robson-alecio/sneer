package wheel.reactive.impl;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;

public class SourceImpl<VO> implements Source<VO> {

	class MyOutput extends AbstractSignal<VO> {

		@Override
		public VO currentValue() {
			return _currentValue;
		}

	}


	class MySetter implements Omnivore<VO> {

		@Override
		public void consume(VO newValue) {
			if (isSameValue(newValue)) throw new IllegalArgumentException("New value must be different.");
			_currentValue = newValue;
			_output.notifyReceivers(newValue);
		}
		
	}


	private VO _currentValue;
	private final Omnivore<VO> _setter = new MySetter();
	private final AbstractSignal<VO> _output = new MyOutput();
	
	
	public SourceImpl(VO initialValue) {
		if (initialValue == null) return;
		setter().consume(initialValue);
	}


	public boolean isSameValue(VO value) {
		if (value == _currentValue) return true; 
		if (value != null && value.equals(_currentValue)) return true;
		return false;
	}


	public Signal<VO> output() {
		return _output;
	}


	public Omnivore<VO> setter() {
		return _setter;
	}

}
