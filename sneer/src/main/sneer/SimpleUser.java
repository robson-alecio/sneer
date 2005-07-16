package sneer;

import java.io.IOException;

import sneer.Sneer.User;

public abstract class SimpleUser implements User {

	public String confirmName(String currentName) {
		return answer("Your name", "Sneer User");
	}

	public int confirmServerPort(int currentPort) {
		acknowledge(
			"To connect to you, your friends will have to know" +
			" your IP address.\n" +
			"\n" +
			"To discover your own IP address, you" +
			" can point your browser to www.whatsmyip.org or" +
			" www.whatsmyip.info"
		);
		
		String prompt =
			"To connect to you, your friends will also have to" +
			" know your IP port number. You can change it if you know" +
			" what you're doing.\n" +
			"\n" +
			"Suggested port number:";

		String portString = answer(prompt, "" + currentPort);
		return Integer.parseInt(portString);
	}

	public String thoughtOfDay(String currentThought) {
		return answer("Thought of the Day", currentThought);
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

	abstract protected String answer(String prompt, String defaultAnswer);

	abstract protected void acknowledge(String fact);

	abstract protected void lamentError(String message);

}
