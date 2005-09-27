package sneer.remote;


public class ObjectSmokeSignal extends AbstractSmokeSignal {

	private final Object _newValue;

	ObjectSmokeSignal(int indianId, Object newValue) {
		super(indianId);
		_newValue = newValue;
	}
	
	Object newValue() {
		return _newValue;
	}

	private static final long serialVersionUID = 1L;

}
