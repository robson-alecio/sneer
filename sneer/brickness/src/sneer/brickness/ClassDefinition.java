package sneer.brickness;

public class ClassDefinition extends Tuple {

	public final String name;
	public final byte[] bytes;

	public ClassDefinition(String name_, byte[] bytes_) {
		name = name_;
		bytes = bytes_;
	}
}
