package sneer.kernel;

import static sneer.kernel.SneerDirectories.sneerDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import sneer.kernel.install.Dialog;
import wheel.io.ui.User;

public class NameChange {

	public NameChange(User user, boolean forceChange) {
		try {
			if (hasName() && !forceChange) return;
			
			changeOwnName(user);
		} catch (IOException e) {
			user.acknowledgeUnexpectedProblem(e.getMessage());
		}
	}
	
	private void changeOwnName(User user) throws IOException {
		String name = user.answer(" Digite seu nome", name());
		if (name == null || name.trim().isEmpty()) return;
		name = name.trim();
	
		saveName(name);
	}

	private boolean hasName() throws IOException {
		return name() != null;
	}

	private String name() throws IOException  {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(nameFile()));
			return reader.readLine().trim();
		} catch (FileNotFoundException ignored) {
			return null;
		} finally {
			if (reader != null) reader.close();
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
