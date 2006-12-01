package sneer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;

import javax.swing.JOptionPane;

public class Bootstrap2 {

	private static File _mainApp;
	private static final String PREFIX = "main";
	private static final String ZERO_MASK = "000000";
	private static final String SUFFIX = ".jar";
	private static final int FILENAME_LENGTH = PREFIX.length() + ZERO_MASK.length() + SUFFIX.length();
	
	private static Socket _socket;
	private static ObjectInputStream _objectIn;
	public static final String UP_TO_DATE = "UP TO DATE";

	private static PrintWriter _log;

	
	public static void main(String[] ignored) {
			try {
				tryToRun();
			} catch (Throwable t) {
				log(t);
			}
	}


	static void log(Throwable throwable) {
			try {
				log("\n" + stackToString(throwable));
			} catch (FileNotFoundException e) {
				show(throwable);
				show(e);
			}
	}


	private static String stackToString(Throwable throwable) {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		throwable.printStackTrace(new PrintWriter(result));
		return result.toString();
	}


	private static PrintWriter log() throws FileNotFoundException {
		if (_log != null) return _log;
		logDirectory().mkdir();
		PrintWriter logWriter = printWriterFor(new File(logDirectory(), "log.txt"));
		return logWriter;
	}


	static PrintWriter printWriterFor(File file) throws FileNotFoundException {
		FileOutputStream logStream = new FileOutputStream(file, true);
		PrintWriter logWriter = new PrintWriter(logStream);
		return logWriter;
	}

	
	private static void show(Throwable t) {
		t.printStackTrace();
		JOptionPane.showMessageDialog(null, t.toString(), "Sneer - Unexpected Problem", JOptionPane.ERROR_MESSAGE);
	}


	private static void tryToRun() throws Exception {
		checkJavaVersionOtherwiseExit();
		
		if (!hasMainApp()) tryToInstallMainAppOtherwiseExit();
		runMainApp();
	}

	
	static void checkJavaVersionOtherwiseExit() {
		String version = System.getProperty("java.specification.version");
		System.out.println(version);
		if (Float.parseFloat(version) >= 1.6f) return;
		
		showOptionDialog("Você precisa usar Java versão 6 ou mais recente.", "Sair");
		System.exit(-1);
	}


	private static void runMainApp() throws Exception {
		Class<?> clazz = new URLClassLoader(new URL[] { mainApp().toURI().toURL() }).loadClass("sneer.Main");
		clazz.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { new String[0] });
	}

	
	private static boolean hasMainApp() {
		return mainApp() != null;
	}

	
	static File mainApp() {
		if (_mainApp == null) _mainApp = findNewestMainApp(programsDirectory());
		return _mainApp;
	}

	
	static File findNewestMainApp(File directory) {
		int newest = 0;
		for (String filename : listFilenames(directory))
			if (validNumber(filename) > newest) newest = validNumber(filename);  
		
		if (newest == 0) return null;
		return new File(directory, PREFIX + zeroPad(newest) + SUFFIX);
	}

	
	private static String[] listFilenames(File directory) {
		String[] result = directory.list();
		if (result == null) return new String[0];
		return result;
	}

	
	private static String zeroPad(int fileNumber) {
		String concat = ZERO_MASK + fileNumber;
		return concat.substring(concat.length() - ZERO_MASK.length());
	}

	
	static int validNumber(String mainAppCandidate) {
		if (!mainAppCandidate.startsWith(PREFIX)) return -1;
		if (!mainAppCandidate.endsWith(SUFFIX)) return -1;
		if (mainAppCandidate.length() != FILENAME_LENGTH) return -1;
		
		try {
			return Integer.parseInt(mainAppCandidate.substring(PREFIX.length(), PREFIX.length() + ZERO_MASK.length()));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	
	private static void tryToInstallMainAppOtherwiseExit() throws IOException {
		if (!sneerDirectory().exists()) {
			approveInstallationWithUserOtherwiseExit();
			tryToCreateSneerDirectory();
		}
		tryToDownloadMainAppVersion(1);
	}


	private static void approveInstallationWithUserOtherwiseExit() {
		
		approveConditionWithUserOtherwiseExit(
				" Bem-vind@ ao Sneer, o peer soberano.\n\n" +
				" Pioneir@ da computação soberana, você poderá contar\n" +
				" para seus netos que participou da revolução desde a\n" +
				" primeira versão da primeira plataforma soberana.",
				"Legal  :)"
		);

		approveConditionWithUserOtherwiseExit(
				" Esta primeira versão é pouco mais que vaporware,\n" +
				" exibe informações técnicas detalhadas e só faz\n" +
				" sentido pra usuários avançados.\n\n" +
				" Por favor, não a divulgue fora desse público.",
				"Tá bom"
		);

		approveConditionWithUserOtherwiseExit(
				" Esta versão mínima do Sneer tem uma única funcionalidade:\n" +
				" conectar-se ao servidor do projeto, baixar e executar suas atualizações.\n\n" +
				" Isso será feito automaticamente. Tratando-se do Sneer e todos seus\n" +
				" plugins soberanos, portanto, você nunca mais vai precisar instalar nada\n" +
				" para este usuário, nesta máquina.",
				"Massa"
		);

		approveConditionWithUserOtherwiseExit(
				" Futuramente, o Sneer será capaz de baixar suas atualizações com\n" +
				" segurança, através de qualquer contato soberano que você tenha.\n\n" +
				" Por enquanto, o Sneer vai baixar atualizações, quando houver, de\n" +
				" klaus.selfip.net na porta 4242.\n\n" +
				" Ainda não há checagem de segurança ou criptografia alguma para as\n" +
				" atualizações. A segurança é a mesma que você tem quando baixa\n" +
				" executáveis de sites web sem o cadeadinho (ex.: Sourceforge).\n\n" +
				" Beleza?",
				"Beleza", "Nem a pau"
		);

		approveConditionWithUserOtherwiseExit(
				" As tentativas de atualização são logadas num arquivo de log fácil de\n" +
				" achar, dentro do diretório do Sneer.\n\n" +
				" Não entre em pânico. O servidor de atualizações nem sempre está ligado.",
				"OK"
		);

		approveConditionWithUserOtherwiseExit(
				" Quando não encontra atualização, o Sneer simplesmente encerra em silêncio. Não\n" +
				" consome recursos da máquina, portanto.\n\n" +
				" Por favor, coloque o Sneer para ser executado na inicialização da sua máquina.\n" +
				" Linux: Chamar \"java -jar Sneer.jar\" num runlevel script.\n" +
				" Windows: Criar atalho para Sneer.jar no menu \"Iniciar > Programas > Inicializar\"",
				"Já coloquei", "Não sei colocar"
		);

		approveConditionWithUserOtherwiseExit(
				" Sabe aqueles textos gigantescos e pentelhos de licença, que aparecem\n" +
				" na instalação de todo software e que ninguém lê?\n\n" +
				" Usando software soberano, você nunca mais vai precisar ver um texto\n" +
				" daquele na sua frente.",
				" Uhu! Por que não?"
		);
		
		approveConditionWithUserOtherwiseExit(
				" O Sneer é software livre, licenciado pela General Public License 2 (GPL 2):\n" +
				" http://www.gnu.org/copyleft/gpl.html\n\n" +
				" Todas as versões do Sneer e de todos os plugins soberanos, que também\n" +
				" forem licenciados pela GPL 2, serão instalados e executados sem exibir o\n" +
				" texto da licença.\n\n" +
				" Você aceita esses termos?",
				"Claro", "Claro que não"
		);

		approveConditionWithUserOtherwiseExit(
				" Será criado um diretório chamado \".sneer\" dentro do diretório\n" +
				" " + userHome() + "\n\n" + 
				" Lá serão guardados, para este usuário, todos os dados e\n" +
				" programas do Sneer.\n\n" +
				" Este diálogo não será exibido novamente enquanto existir esse\n" +
				" diretório.",
				"Criar diretório", "Não criar"
		);
		
		sayGoodbye();

	}


	static void sayGoodbye() {
		showOptionDialog(
				" Obrigado e até a próxima atualização do Sneer.  ;)",
				"Falou"
		);
	}


	private static void approveConditionWithUserOtherwiseExit(String condition, Object... options) {
		boolean approved = showOptionDialog(condition, options);
		if (!approved) exit();
	}


	static boolean showOptionDialog(String message, Object... options) {
		int chosen = JOptionPane.showOptionDialog(null, message + "\n\n", "Sneer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		return chosen == 0;
	}


	private static void exit() {
		showOptionDialog(
				" O Sneer será encerrado sem alterações em seu sistema.\n\n" +
				" Por favor, conte-nos o motivo da sua desistência na\n" +
				" lista do sneercoders no googlegroups.",
				"Falou"
		);
		System.exit(0);
	}


	private static void tryToCreateSneerDirectory() throws IOException {
		if (!sneerDirectory().mkdir())
			throw new IOException("Unable to create Sneer directory\n" + sneerDirectory());
	}


	static void tryToDownloadMainAppVersion(int version) throws IOException {
		int mainAppVersion;
		byte[] mainAppContents;
		try {
			openDownloadConnectionForVersion(version);
			Object received = receiveObject();
			if (UP_TO_DATE.equals(received)) {
				log("Servidor encontrado. Não há atualização para o Sneer no momento.");
				return;
			}

			log("Servidor encontrado. Baixando atualização para o Sneer...");
			mainAppVersion = (Integer)received;
			mainAppContents = (byte[])receiveObject();
		} finally {
			closeDownloadConnection();
		}

		writeToMainAppFile(mainAppVersion, mainAppContents);
		log("Atualização baixada.");
	}

	
	private static void log(String entry) throws FileNotFoundException {
		log().println("" + new Date() + "  " + entry);
		log().println();
		log().println();
		log().flush();
	}


	private static void writeToMainAppFile(int version, byte[] contents) throws IOException {
		programsDirectory().mkdir();
		File part = new File(programsDirectory(), "sneer.part");
		FileOutputStream fos = new FileOutputStream(part);
		fos.write(contents);
		fos.close();
		
		part.renameTo(new File(programsDirectory(), PREFIX + zeroPad(version) + SUFFIX));
	}

	
	static private File logDirectory() {
		return new File(sneerDirectory(), "logs");
	}

	static private File programsDirectory() {
		return new File(sneerDirectory(), "programs");
	}
	
	
	static File sneerDirectory() {
		return new File(userHome(), ".sneer");
	}


	private static String userHome() {
		return System.getProperty("user.home");
	}

	
	private static void openDownloadConnectionForVersion(int version) throws IOException {
		_socket = new Socket("klaus.selfip.net", 4242);
		
		new ObjectOutputStream(_socket.getOutputStream()).writeObject(version);
		
		_objectIn = new ObjectInputStream(_socket.getInputStream());
	}

	
	private static void closeDownloadConnection() {
		try {
			if (_socket != null) _socket.close();
		} catch (IOException ignored) {}

		_objectIn = null;
		_socket = null;
	}
	
	
	static private Object receiveObject() throws IOException {
		try {
			return _objectIn.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	
}
