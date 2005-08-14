//Copyright (C) 2005 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.foundation.Cool;
import org.prevayler.foundation.network.OldNetwork;

import sneer.life.JpgImage;
import sneer.life.Life;
import sneer.life.LifeView;
import sneer.remote.ParallelServer;

public class Sneer {
	
	private static final String SNEER_DIRECTORY = System.getProperty("user.home") + File.separator + ".sneer";

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
	}
	
	private final Life _life;
	private final User _user;
	private Set<String> _knownMessages = new HashSet<String>();

	private final Prevayler _prevayler;

	private ParallelServer _server;
	private final OldNetwork _network;

	private Home _home;


	public Sneer(User user, OldNetwork network) throws IOException {
		if (null == user) throw new IllegalArgumentException();
		_user = user;
		_network = network;

		Home._network = _network; //FIXME: Remove this static dependency. Transaction journal should be recoverable regardless of the network.
		
		_prevayler = prevayler();
		_home = (Home)_prevayler.prevalentSystem();

		if (_home.life() == null) getALife(); 
		_life = _home.life();

		
		startUserNotificationDaemon();
		startServer();
	}

	private Prevayler prevayler() throws IOException {
		try {
			return PrevaylerFactory.createPrevayler(new Home(), SNEER_DIRECTORY);
		} catch (ClassNotFoundException e) {
			throw new IOException("Class not found: " + e.getMessage());
		}
	}

	private void getALife() {
		_prevayler.execute(new Birth(_user));
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
		_prevayler.execute(new ContactAddition(_user));
		_user.lookAtMe();
	}

	public void editPersonalInfo() {
		_prevayler.execute(new PersonalInfoEditting(_user, _life));
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
}