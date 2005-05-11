package sneer;

public class Sneer {
	
	public interface User {
		String promptName();
	}
	
	private final Life _life;
	private final User _user;

	public Sneer(User user) {
		if (null == user) throw new IllegalArgumentException();
		_user = user;
		_life = new LifeImpl(_user.promptName());
	}
	
	public Life life() {
		return _life;
	}
}