package sneer.brickness;

public class ClassDefinition extends Tuple {

	public final byte[] _bytes;
	public final String _name;

	public ClassDefinition(String name, byte[] bytes) {
		_name = name;
		_bytes = bytes;
	}

	public byte[] bytes() {
		return _bytes;
	}

	public String name() {
		return _name;
	}
}
