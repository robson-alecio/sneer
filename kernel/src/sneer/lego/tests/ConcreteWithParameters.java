package sneer.lego.tests;

public class ConcreteWithParameters {

	private String _s;
	
	private int _i;
	
	private SomeInterface _interface;
	
	public ConcreteWithParameters(SomeInterface intrface) {
		_interface = intrface;
	}

	public ConcreteWithParameters(String s) {
		_s = s;
	}
	
	public ConcreteWithParameters(String s, int i) {
		_s = s;
		_i = i;
	}

	public String getS() {
		return _s;
	}

	public int getI() {
		return _i;
	}

	public SomeInterface getInterface() {
		return _interface;
	}
}
