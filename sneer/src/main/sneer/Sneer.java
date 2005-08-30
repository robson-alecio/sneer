//Copyright (C) 2005 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
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
import wheel.experiments.environment.network.OldNetwork;

import com.thoughtworks.xstream.XStream;

public class Sneer {
	
	public interface User {
		String confirmName(String currentName);
		String thoughtOfTheDay(String currentThought);
		JpgImage confirmPicture(JpgImage image);

		String writePublicMessage();
		
		String giveNickname();
		String informTcpAddress(String defaultAddress);

		void lamentException(Exception e);
		void acknowledge(String fact);

		int confirmServerPort(int currentPort);

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


	public Sneer(User user, OldNetwork network, String directory) throws IOException {
		if (null == user) throw new IllegalArgumentException();
		_user = user;
		_network = new XStreamNetwork(new XStream(), network);

		Home._network = _network; //FIXME: Remove this static dependency. Transaction journal should be recoverable regardless of the network.
		_prevayler = prevayler(directory);
		_home = (Home)_prevayler.prevalentSystem();

		if (_home.life() == null) getALife(); 
		_life = _home.life();

		
		startUserNotificationDaemon();
		startServer();
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
		try {
			_server = new ParallelServer(_life, _network.openObjectServerSocket(_home.serverPort()));
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
		execute(new ContactRemoval(nickname));
	}

	public void editPersonalInfo() {
		execute(new PersonalInfoEditting(_user, _life));
	}


	public void sendPublicMessage() {
		String message = _user.writePublicMessage();
		_life.send(message);
	}

	public void checkNewMessages() {
		for (String nickname : _life.nicknames()) {
			LifeView contact = _life.contact(nickname);
			if (contact.lastSightingDate() == null) continue;
			showMessages(nickname, contact.publicMessages());
		}
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
			_user.acknowledge("Message sent from " + sender + ":\n\n" + message);
		}
		
	}

	public void acknowledgeContactOnline(String nickname) {
		_user.acknowledge(nickname + " has come online.");
	}

}