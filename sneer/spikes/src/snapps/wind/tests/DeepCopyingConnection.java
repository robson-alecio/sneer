package snapps.wind.tests;

import snapps.wind.Connection;
import snapps.wind.ConnectionSide;
import wheel.io.serialization.DeepCopier;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class DeepCopyingConnection implements Connection {

	private class Side implements ConnectionSide, Omnivore<Object> {

		private Omnivore<Object> _receiver;
		private Side _otherSide;

		@Override
		public void registerReceiver(Omnivore<Object> receiver) {
			_receiver = receiver;
		}

		@Override
		public Omnivore<Object> sender() {
			return this;
		}

		@Override
		public void consume(Object objectToSend) {
			_trafficCounter.setter().consume(_trafficCounter.output().currentValue() + 1);
			
			Object copy = DeepCopier.deepCopy(objectToSend);
			_otherSide._receiver.consume(copy);
		}

	}

	private final Side _sideA;
	private final Side _sideB;
	
	private Register<Integer> _trafficCounter = new RegisterImpl<Integer>(0);

	
	public DeepCopyingConnection() {
		_sideA = new Side();
		_sideB = new Side();
		
		_sideA._otherSide = _sideB;
		_sideB._otherSide = _sideA;
	}

	@Override
	public ConnectionSide sideA() {
		return _sideA;
	}

	@Override
	public ConnectionSide sideB() {
		return _sideB;
	}

	public Signal<Integer> trafficCounter() {
		return _trafficCounter.output();
	}
	
}
