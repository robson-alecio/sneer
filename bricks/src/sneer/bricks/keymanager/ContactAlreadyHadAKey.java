package sneer.bricks.keymanager;

public class ContactAlreadyHadAKey extends Exception {

	private static final long serialVersionUID = 1L;

	public ContactAlreadyHadAKey(String message) {
		super(message);
	}
}
