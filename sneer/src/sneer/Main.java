package sneer;

import static sneer.strap.SneerDirectories.sneerDirectory;
import static sneer.strap.SneerDirectories.validNumber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import sneer.strap.Dialog;
import sneer.strap.VersionUpdateAttempt;
import wheelexperiments.Log;
import wheelexperiments.environment.ui.User;


public class Main extends sneer.strap.Main {
	
	public static void main(String[] args) {
		try {
			tryToRun();
		} catch (Throwable t) {
			Log.log(t);
		}
	}

	private static void tryToRun() throws IOException {
		freeCallingThreadForClassGarbageCollection();
		
		if (!hasName()) {
			User user = new User();
			enterName(user);
			new Dialog(user).goodbye();
		}
		tryToDownloadNextVersion();
	}

	private static void freeCallingThreadForClassGarbageCollection() {
		return;
	}

	private static void enterName(User user) throws FileNotFoundException {
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


	private static void saveName(String name) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(nameFile());
		writer.println(name);
		writer.close();
	}

	private static boolean hasName() {
		return name() != null;
	}

	private static File nameFile() {
		return new File(sneerDirectory(), "name.txt");
	}

	private static String name()  {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(nameFile()));
			return reader.readLine().trim();
		} catch (IOException e) {
			return null;
		}
	}

	private static void tryToDownloadNextVersion() throws IOException {
		int currentVersion = validNumber(mainApp().getName());
		new VersionUpdateAttempt(currentVersion + 1);
	}

	
}
