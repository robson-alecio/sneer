package sneer.lego.tests;

public class SomeInterfaceImpl implements SomeInterface {

	private int _value;
	
	public SomeInterfaceImpl(int value) {
		_value = value;
	}
	
	@Override
	public int getValue() {
		return _value;
	}

}
