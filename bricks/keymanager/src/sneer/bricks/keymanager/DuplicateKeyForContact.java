package sneer.bricks.keymanager;

public class DuplicateKeyForContact extends Exception {

	private static final long serialVersionUID = 1L;

	public DuplicateKeyForContact(String message) {
		super(message);
	}
}
