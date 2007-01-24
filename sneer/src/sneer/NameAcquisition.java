package sneer;

import static sneer.strap.SneerDirectories.sneerDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import sneer.strap.Dialog;
import wheelexperiments.environment.ui.User;

public class NameAcquisition {

	NameAcquisition(User user) throws FileNotFoundException {
		acquireOwnNameIfNecessary(user);
	}
	
	private void acquireOwnNameIfNecessary(User user) throws FileNotFoundException {
		if (hasName()) return;
	
		enterName(user);
		new Dialog(user).goodbye();
	}

	private void enterName(User user) throws FileNotFoundException {
		String name = user.answer(
			" Você acaba de alcançar a liberdade 1\n" +
			" da computação soberana: Nome Próprio.\n\n" +
			" Digite seu nome"
		);
		if (name == null || name.trim().isEmpty()) return;
		name = name.trim();
	
		saveName(name);
		
		user.choose(
			" Seu nome foi gravado no arquivo:\n" +
			" " + nameFile() + "\n\n" +
			" Para alterá-lo, edite diretamente o arquivo.",
			"Nossa! Que fácil"
		);
		
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
