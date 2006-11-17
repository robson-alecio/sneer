package sneer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;

public class Bootstrap {

	private static File _mainApp;
	private static final String PREFIX = "main";
	private static final String ZERO_MASK = "000000";
	private static final String SUFFIX = ".jar";
	private static final int FILENAME_LENGTH = PREFIX.length() + ZERO_MASK.length() + SUFFIX.length();
	
	private static Socket _socket;
	private static ObjectInputStream _objectIn;
	public static final String GREETING = "Sneer Bootstrap";
	

	public static void main(String[] ignored) {
			try {
				tryToRun();
			} catch (Throwable t) {
				t.printStackTrace();
				JOptionPane.showMessageDialog(null, t.toString(), "Sneer - Unexpected Problem", JOptionPane.ERROR_MESSAGE);
			}
	}

	private int logErrorInLogTxtFile;
	
	private static void tryToRun() throws Exception {
		if (!hasMainApp()) {
			tryToDownloadMainApp();
			if (!hasMainApp()) return;
		}

		runMainApp();
	}

	
	private static void runMainApp() throws Exception {
		Class<?> clazz = new URLClassLoader(new URL[] { mainApp().toURI().toURL() }).loadClass("sneer.Main");
		clazz.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { new String[0] });
	}

	
	private static boolean hasMainApp() {
		return mainApp() != null;
	}

	
	private static File mainApp() {
		if (_mainApp == null) _mainApp = findNewestMainApp();
		return _mainApp;
	}

	
	private static File findNewestMainApp() {
		int newest = 0;
		for (String filename : listFilenames(programsDirectory()))
			if (validNumber(filename) > newest) newest = validNumber(filename);  
		
		if (newest == 0) return null;
		return new File(PREFIX + zeroPad(newest) + SUFFIX);
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

	
	private static int validNumber(String mainAppCandidate) {
		if (!mainAppCandidate.startsWith(PREFIX)) return -1;
		if (!mainAppCandidate.endsWith(SUFFIX)) return -1;
		if (mainAppCandidate.length() != FILENAME_LENGTH) return -1;
		
		try {
			return Integer.parseInt(mainAppCandidate.substring(PREFIX.length(), PREFIX.length() + ZERO_MASK.length()));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	
	private static void tryToDownloadMainApp() throws IOException {
		byte[] jarContents = downloadMainAppJarContents();
		writeToMainAppFile(jarContents);
	}

	
	private static void writeToMainAppFile(byte[] jarContents) throws IOException {
		programsDirectory().mkdirs();
		File part = new File(programsDirectory(), "sneer.part");
		FileOutputStream fos = new FileOutputStream(part);
		fos.write(jarContents);
		fos.close();
		
		part.renameTo(new File(programsDirectory(), PREFIX + zeroPad(1) + SUFFIX));
	}

	
	static private File programsDirectory() {
		return new File(sneerDirectory(), "programs");
	}
	
	
	static private File sneerDirectory() {
		return new File(System.getProperty("user.home"), ".sneer");
	}

	
	private static byte[] downloadMainAppJarContents() throws IOException {
		try {
			openDownloadConnection();
			return receiveByteArray();
		} finally {
			closeDownloadConnection();
		}
	}

	
	private static void openDownloadConnection() throws IOException {
		_socket = new Socket("klaus.selfip.net", 4242);
		
		new ObjectOutputStream(_socket.getOutputStream()).writeObject(GREETING);
		
		_objectIn = new ObjectInputStream(_socket.getInputStream());
	}

	
	private static void closeDownloadConnection() {
		try {
			if (_socket != null) _socket.close();
		} catch (IOException ignored) {}

		_objectIn = null;
		_socket = null;
	}
	
	
	static private byte[] receiveByteArray() throws IOException {
		try {
			return (byte[])_objectIn.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	
	//Bem-vind@ ao Sneer, o peer soberano.
	//Você é um(a) pioneir@ da computação soberana e poderá contar pros seus netos que participou da revolução desde a primeira versão da primeira plataforma soberana.
	//Legal  :)

	//Esta primeira versão exibe informações técnicas detalhadas e só faz sentido pra usuários BEM avançados.
	//Por favor, não a divulgue fora desse público.
	//Tá bom

	//Sabe aqueles textos gigantescos e pentelhos de licença, que aparecem na instalação de todo software e que ninguém lê?
	//Usando software soberano, você nunca mais vai precisar ver um texto daquele na sua frente.
	//Uhu! Por que não?
	
	//O Sneer é software livre, licenciado pela General Public License 2 (GPL 2):
	//http://www.gnu.org/copyleft/gpl.html
	//Todas as versões do Sneer e de todos os plugins soberanos, que também forem licenciados pela GPL 2, serão instalados sem exibir o texto da licença.
	//Você aceita esses termos?
	//Aceito | Não Aceito

	//Será criado um diretório chamado ".sneer" dentro do diretório ><><><><><><><><><><><>.
	//Todos os dados e programas do Sneer, quando houver, para este usuário, serão guardados lá.
	//Este diálogo não será exibido novamente enquanto existir esse diretório.
	//Criar diretório | Não criar

	//Esta versão mínima do Sneer tem uma única funcionalidade: conectar-se ao servidor do projeto, baixar e executar as suas atualizações.
	//Isso será feito automaticamente, ou seja: você não precisa instalar manualmente, para este usuário, nesta máquina, mais nada relacionado ao Sneer ou qualquer de seus plugins.
	//Legal
	
	//Futuramente, o Sneer será capaz de baixar suas atualizações com segurança, através de qualquer contato soberano que você tenha.
	//Por enquanto, o Sneer vai baixar atualizações, quando houver, de klaus.selfip.net na porta 4242.
	//Ainda não há checagem de segurança ou criptografia alguma para as atualizações. A segurança é a mesma que você tem quando baixa executáveis de sites web sem o cadeadinho (ex: sourceforge).
	//Beleza?
	//Beleza | Não

	//Quando não encontra atualização, o Sneer simplesmente encerra em silêncio. Não consome recursos da máquina, portanto.
	//Por favor coloque o Sneer para ser executado quando sua máquina é iniciada.
	//Linux: Chamar "java -jar Sneer.jar" num Runlevel Script.
	//Windows: Criar atalho para Sneer.jar no menu "Iniciar > Programas > Inicializar".
	//Já coloquei
	
	//As tentativas de atualização serão logadas em log.txt dentro do diretório do Sneer.
	//Não entre em pânico. O servidor de atualizações nem sempre está ligado.
	//OK
	
	//Obrigado e até a próxima atualização do Sneer.  :)
	//Sair
	
	//O Sneer será encerrado sem alterações a seu sistema. Por favor, informe-nos o motivo da sua desistência na lista do sneercoders no googlegroups.
	//Sair
	
}
