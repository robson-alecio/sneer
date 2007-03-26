package sneer.kernel;

import static sneer.kernel.SneerDirectories.sneerDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.prevayler.Transaction;

import sneer.kernel.install.Dialog;
import wheel.io.ui.User;

public class NameChange implements Transaction {

	private final String _name;

	public NameChange(User user, Domain domain) {
		_name = user.answer(" What is your name?" +
			"\n (You can change it any time you like)", domain.ownName());
	}
	
	public void executeOn(Object domain, Date ignored) {
		((Domain)domain).ownName(_name);
	}
	

	private static final long serialVersionUID = 1L;
}
