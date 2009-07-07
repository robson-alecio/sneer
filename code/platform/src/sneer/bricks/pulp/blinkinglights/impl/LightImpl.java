package sneer.bricks.pulp.blinkinglights.impl;

import sneer.bricks.pulp.blinkinglights.Light;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.foundation.lang.Consumer;

class LightImpl implements Light {
	
	static final int NEVER = 0;
	
	boolean _isOn = false;

	private final LightType _type;
	private final Consumer<Boolean> _confirmationReceiver;
	
	String _caption;
	Throwable _error;
	String _helpMessage;

	
	public LightImpl(LightType type) {
		this(type, null);
	}

	public LightImpl(LightType type, Consumer<Boolean> confirmationReceiver) {
		_type = type;
		_confirmationReceiver = confirmationReceiver;
	}

	@Override public Throwable error() { return _error; }
	@Override public boolean isOn() { return _isOn; }
	@Override public String caption() { return _caption; }
	@Override public LightType type() { return _type; }
	@Override public String helpMessage() { return _helpMessage; }
	
	@Override public boolean hasConfirmation() { return _confirmationReceiver!=null; }
	@Override public void sayNo() { _confirmationReceiver.consume(false);}
	@Override public void sayYes() { _confirmationReceiver.consume(true);}

	void turnOff() {
		_isOn = false;
	}
}