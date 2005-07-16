package sneer;

import java.io.IOException;

import sneer.Sneer.User;

public abstract class SimpleUser implements User {

	public String confirmName(String currentName) {
		return answer("Your name", "Sneer User");
	}

	public String giveNickname() {
		return answer("Give your new contact a nickname", "friend");
	}

	public String informTcpAddress(String defaultAddress) {
		return answer("What is your contact's address? host:port", defaultAddress);
	}

	public void lamentException(IOException e) {
		e.printStackTrace();
		lamentError(e.toString());
	}

	public String thoughtOfDay(String currentThought) {
		return answer("Thought of the Day", currentThought);
	}

	abstract protected String answer(String prompt, String defaultAnswer);

	abstract protected void lamentError(String message);

}
