package sneer.bricks.keymanager;

public class KeyBelongsToOtherContact extends Exception {

	private static final long serialVersionUID = 1L;

	public KeyBelongsToOtherContact(String message) {
		super(message);
	}
}
