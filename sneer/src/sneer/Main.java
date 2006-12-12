package sneer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import sneer.boot.Dialog;
import sneer.boot.User;
import sneer.boot.VersionUpdateAttempt;
import wheelexperiments.Log;

import static sneer.boot.SneerDirectories.*;



public class Main extends sneer.boot.Boot {
	
	public static void main(String[] args) {
		try {
			tryToRun();
		} catch (Throwable t) {
			Log.log(t);
		}
	}

	private static void tryToRun() throws IOException {
		if (!hasName()) {
			User user = new User();
			user.choose("Foi encontrada uma nova atualização do Sneer.", "Já?");
			enterName(user);
			new Dialog(user).goodbye();
		}
		tryToDownloadNextVersion();
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
			return reader.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	private static void tryToDownloadNextVersion() throws IOException {
		int currentVersion = validNumber(mainApp().getName());
		new VersionUpdateAttempt(currentVersion + 1);
	}

	
}
