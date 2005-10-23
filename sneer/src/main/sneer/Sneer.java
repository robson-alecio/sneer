//Copyright (C) 2005 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.BindException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.Transaction;

import sneer.life.JpgImage;
import sneer.life.Life;
import sneer.life.LifeView;
import sneer.remote.ParallelServer;
import sneer.remote.xstream.XStreamNetwork;
import wheel.experiments.Cool;
import wheel.experiments.environment.network.ObjectServerSocket;
import wheel.experiments.environment.network.ObjectSocket;
import wheel.experiments.environment.network.OldNetwork;

import com.thoughtworks.xstream.XStream;

public class Sneer {
	
	public interface User {
		String confirmName(String currentName);
		String thoughtOfTheDay(String currentThought);
		JpgImage confirmPicture(JpgImage image);

		String writeMessage();
		
		String giveNickname();
		String informTcpAddress(String defaultAddress);

		void lamentException(Exception e);
		void lamentError(String error, String help);
		void acknowledge(String fact);
		boolean confirm(String proposition);

		int confirmServerPort(int currentPort);
		
		void receiveMessage(String message, String sender);

		void lookAtMe();
		
		void goodbye();
	}
	
	private final Life _life;
	private final User _user;
	private Set<String> _knownMessages = new HashSet<String>();

	private final Prevayler _prevayler;

	private ParallelServer _server;
	private final OldNetwork _network;

	private Home _home;
	static private PrintStream _log;


	public Sneer(User user, OldNetwork network, String directory) throws IOException {
		if (null == user) throw new IllegalArgumentException();
		_user = user;
		_network = new XStreamNetwork(new XStream(), network);

		
try {
	testEclipseSandbox();
} catch (Exception e) {
	log(e);
}

		
		Home._network = _network; //FIXME: Remove this static dependency to Home. Transaction journal should be recoverable regardless of the network.
		_prevayler = prevayler(directory);
		_home = (Home)_prevayler.prevalentSystem();

		if (_home.life() == null) getALife(); 
		_life = _home.life();

		startUserNotificationDaemon();
		startServer();
		
	}

	private void testEclipseSandbox() throws IOException, ClassNotFoundException {
		final ObjectServerSocket server = _network.openObjectServerSocket(6070);
		Cool.startDaemon(new Runnable(){
			public void run() {
				try {
					ObjectSocket serverSocket = server.accept();
					produceLog().println(serverSocket.readObject());
					serverSocket.writeObject("Goodbye");
				} catch (Exception e) {
					log(e);
				}
			}
		});
		ObjectSocket clientSocket = _network.openSocket("localhost", 6070);
		clientSocket.writeObject("Hello");
		produceLog().println(clientSocket.readObject());
	}

	private Prevayler prevayler(String directory) throws IOException {
		try {
			return PrevaylerFactory.createPrevayler(new Home(), directory);
		} catch (ClassNotFoundException e) {
			throw new IOException("Class not found: " + e.getMessage());
		}
	}

	private void getALife() {
		execute(new Birth(_user));
	}

	private void execute(Transaction transaction) {
		_prevayler.execute(transaction);
		_user.lookAtMe();
	}

	private void startServer() {
		int port = _home.serverPort();
		try {
			_server = new ParallelServer(_life, _network.openObjectServerSocket(port));
		} catch (BindException ignored) {
			_user.lamentError("Port " + port + " is already being used by another application.", "You can have two instances of Sneer running if you like, for two people for example, but each one has to use a different port. If there is another application using that same port, you have either to close it, configure it to use a different port, or configure Sneer to use a different port.");
		} catch (IOException e) {
			_user.lamentException(e);
		}
	}


	private void startUserNotificationDaemon() {
		Cool.startDaemon(new Runnable() {
			public void run() {
				while (true) {
					_user.lookAtMe();
					Cool.sleep(1000*3);
				}				
			}
		});
	}
	
	public Life life() {
		return _life;
	}

	public void addContact() {
		executeWizard(new ContactAddition(_user));
	}

	private void executeWizard(ContactAddition addition) {
		if (addition.cancelled()) return;
		execute(addition);
	}

	public void removeContact(String nickname) {
		if (!_life.nicknames().currentElements().contains(nickname)) {
			_user.acknowledge("" + nickname + " is not one of your first level contacts.");
			return;
		}
		execute(new ContactRemoval(nickname));
	}

	public void editPersonalInfo() {
		try {
			execute(new PersonalInfoEditting(_user, _life));
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void checkNewMessages() {
		for (String nickname : _life.nicknames().currentElements()) {
			LifeView contact = _life.contact(nickname);
			if (contact == null) continue;
			if (contact.lastSightingDate() == null) continue;
			if (allSentMessages(contact) == null) continue;
			List<String> messages = allSentMessages(contact).get(_life.name().currentValue());
			if (messages == null) continue;
			showMessages(nickname, messages);
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, List<String>> allSentMessages(LifeView contact) {
		Map<String, List<String>> allMessages = (Map<String, List<String>>)contact.thing("Messages");
		return allMessages;
	}
	
	public void close() {
		try {
			_server.close();
		} catch (IOException e) {
			// Sneer is closing anyway.
		}
	}

	private void showMessages(String sender, List<String> messages) {
		for (String message : messages) {
		if (_knownMessages.contains(message)) continue;
			_knownMessages.add(message);
			_user.receiveMessage(message, sender);
		}
		
	}

	public void acknowledgeContactOnline(String nickname) {
		_user.acknowledge(nickname + " has come online.");
	}

	public void stop() {
		_user.goodbye();
	}

	public void sendMessage(String nickname, String message) {
		LifeView lifeView = _life.contact(nickname);
		if (message == null || message.length() == 0) return;
		List<String> messages = getMessagesTo(lifeView);
		messages.add(message);
	}

	private List<String> getMessagesTo(LifeView contact) {
		List<String> messagesToContact = allMySentMessages().get(contact.name().currentValue());
		if (messagesToContact == null) {
			allMySentMessages().put(contact.name().currentValue(), new ArrayList<String>());
			return getMessagesTo(contact);
		}
		return messagesToContact;
	}

	private Map<String, List<String>> allMySentMessages() {
		if (allSentMessages(_life) == null) {
			_life.thing("Messages", new HashMap<String, List<String>>());
			return allMySentMessages();
		}
		return allSentMessages(_life);
	}

	synchronized public static void log(Exception x) {
		PrintStream log = produceLog();
		log.println("===========================================================================");
		log.println(new Date());
		log.println();
		x.printStackTrace(log);
	}

	private static PrintStream produceLog() {
		if (_log != null) return _log;
	
		try {
			File file = new File("SneerExceptions.txt");
			System.out.println("Exceptions are being logged here: " + file.getAbsolutePath());
			return _log = new PrintStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}