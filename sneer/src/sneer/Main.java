package sneer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;


public class Main extends sneer.boot.Boot {
	
	private int uncomment;
//
//	public static void main(String[] args) {
//		try {
//			tryToRun();
//		} catch (Throwable t) {
//			logOtherwiseShow(t);
//		}
//	}
//
//	private static void tryToRun() throws IOException {
//		if (!hasName()) {
//			showOptionDialog("Foi encontrada uma nova atualização do Sneer.", "Já?");
//			enterName();
//			sayGoodbye();
//		}
//		tryToDownloadNextVersion();
//	}
//
//	private static void enterName() throws FileNotFoundException {
//		String prompt =
//			" Você acaba de alcançar a liberdade 1\n" +
//			" da computação soberana: Nome Próprio.\n\n" +
//			" Digite seu nome";
//		String name = JOptionPane.showInputDialog(prompt);
//		if (name == null || name.trim().isEmpty()) return;
//		name = name.trim();
//
//		saveName(name);
//		
//		showOptionDialog(
//			" Seu nome foi gravado no arquivo:\n" +
//			" " + nameFile() + "\n\n" +
//			" Para alterá-lo, edite diretamente o arquivo.",
//			"Nossa! Que fácil"
//		);
//		
//	}
//
//	private static void saveName(String name) throws FileNotFoundException {
//		PrintWriter writer = printWriterFor(nameFile());
//		writer.println(name);
//		writer.close();
//	}
//
//	private static boolean hasName() {
//		return name() != null;
//	}
//
//	private static File nameFile() {
//		return new File(sneerDirectory(), "name.txt");
//	}
//
//	private static String name()  {
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(nameFile()));
//			return reader.readLine();
//		} catch (IOException e) {
//			return null;
//		}
//	}
//
//	private static void tryToDownloadNextVersion() throws IOException {
//		int currentVersion = validNumber(mainApp().getName());
//		tryToDownloadMainAppVersion(currentVersion + 1);
//	}
//
	
}
