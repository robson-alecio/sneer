package sneer;

import static sneer.strap.SneerDirectories.sneerDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import sneer.strap.Dialog;
import wheel.io.ui.User;

public class NameChange {

	NameChange(User user, boolean forceChange) throws FileNotFoundException {
		if (hasName() && !forceChange) return;
		
		changeOwnName(user);
	}
	
	private void changeOwnName(User user) throws FileNotFoundException {
		String name = user.answer(" Digite seu nome", name());
		if (name == null || name.trim().isEmpty()) return;
		name = name.trim();
	
		saveName(name);
	}

	private boolean hasName() {
		return name() != null;
	}

	private String name()  {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(nameFile()));
			return reader.readLine().trim();
		} catch (IOException e) {
			return null;
		}
	}

	private File nameFile() {
		return new File(sneerDirectory(), "name.txt");
	}

	private void saveName(String name) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(nameFile());
		writer.println(name);
		writer.close();
	}

}
