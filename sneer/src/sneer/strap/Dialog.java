package sneer.strap;

import wheelexperiments.environment.ui.User;

public class Dialog {

	protected final User _user;

	public Dialog(User user) {
		_user = user;
	}

	protected void quit() {
		exit(" O Sneer será encerrado sem alterações em seu sistema.\n\n" +
				" Por favor, conte-nos o motivo da sua desistência na\n" +
				" lista do sneercoders no googlegroups."
		);
	}

	void exit(String goodbyeMessage) {
		_user.choose(goodbyeMessage, "Sair");
		System.exit(0);
	}

	public void goodbye() {
		_user.choose(
			" Obrigado e até a próxima atualização do Sneer.  ;)",
			"Falou"
		);
	}

	protected void approveConditionOtherwiseExit(String condition, Object... options) {
		boolean approved = _user.choose(condition, options);
		if (!approved) quit();
	}

}
