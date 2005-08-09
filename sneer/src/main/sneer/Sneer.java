//Copyright (C) 2005 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.prevayler.foundation.Cool;
import org.prevayler.foundation.network.OldNetwork;

import sneer.life.Life;
import sneer.life.LifeImpl;
import sneer.life.LifeView;
import sneer.remote.Connection;
import sneer.remote.ParallelServer;

public class Sneer {
	
	static final int DEFAULT_PORT = 5905;

	public interface User {
		String confirmName(String currentName);
		int confirmServerPort(int currentPort);
		String thoughtOfTheDay(String currentThought);
		
		String giveNickname();
		String informTcpAddress(String defaultAddress);

		void lookAtMe();

		void lamentException(Exception e);
		void acknowledge(String fact);
		String writePublicMessage();
	}
	
	private final Life _life;
	private final User _user;
	private final OldNetwork _network;
	private Set<String> _knownMessages = new HashSet<String>();
	private ParallelServer _server;

	public Sneer(User user, OldNetwork network) throws IOException {
		if (null == user) throw new IllegalArgumentException();
		_user = user;
		_network = network;
		_life = new LifeImpl(confirmName());

		restoreContacts();
		restorePersonalInfo();
		
		startUserNotificationDaemon();
		startServer();
	}

	private void restorePersonalInfo() {
		String thoughtOfTheDay = loadMainProperty("thoughtOfTheDay", "");
		_life.thoughtOfTheDay(thoughtOfTheDay);
	}

	private String confirmName() throws IOException {
		String name = loadMainProperty("name", "Sneer User");
		name = _user.confirmName(name);
		storeMainProperty("name", name);
		return name;
	}	

	private void restoreContacts() {
		Properties p = loadProperties(nicknamesFile());

		for (Entry entry : p.entrySet()) {
			String nickname = (String)entry.getKey();
			String tcpAddress = (String)entry.getValue();
			addContact(nickname, tcpAddress);
		}
	}

	private void startServer() {
		try {
			_server = new ParallelServer(_life, _network.openObjectServerSocket(serverPort()));
		} catch (IOException e) {
			_user.lamentException(e);
		}
	}

	private int serverPort() throws IOException {
		int port = Integer.parseInt(loadMainProperty("serverPort", Integer.toString(DEFAULT_PORT)));
		port = _user.confirmServerPort(port);

		storeMainProperty("serverPort", Integer.toString(port));
		
		return port;
	}

	private void storeMainProperty(String key, String value) throws IOException {
		Properties p = loadProperties(mainPropertiesFile());
		p.setProperty(key, value);
		p.store(new FileOutputStream(mainPropertiesFile()), "");
	}

	private String loadMainProperty(String key, String defaultValue) {
		Properties p = loadProperties(mainPropertiesFile());
		return p.getProperty(key, defaultValue);
	}

	private Properties loadProperties(File propertiesFile) {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(propertiesFile));
		} catch (FileNotFoundException ignored) {
		} catch (IOException e) {
			_user.lamentException(e);
		}
		return p;
	}

	static File mainPropertiesFile() {
		return new File(homeDirectory(), ".sneer");
	}

	private static String homeDirectory() {
		return System.getProperty("user.home");
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
		String nickname = _user.giveNickname();
		String tcpAddress = _user.informTcpAddress("localhost:" + DEFAULT_PORT);
		Properties p1 = new Properties();
		try {
			p1.load(new FileInputStream(nicknamesFile()));
		} catch (FileNotFoundException ignored) {
		} catch (IOException e1) {
			_user.lamentException(e1);
		}
		
		Properties p = p1;
		
		p.setProperty(nickname, tcpAddress);
		try {
			p.store(new FileOutputStream(nicknamesFile()), "");
		} catch (IOException e) {
			_user.lamentException(e);
			return;
		}

		addContact(nickname, tcpAddress);
	}

	static File nicknamesFile() {
		return new File(homeDirectory(), ".sneernicknames");
	}

	private void addContact(String nickname, String tcpAddress) {
		_life.giveSomebodyANickname(remoteContact(tcpAddress), nickname);
		_user.lookAtMe();
	}
	
	public void editPersonalInfo() {
		String thoughtOfTheDay = loadMainProperty("thoughtOfTheDay", _life.thoughtOfTheDay());
		thoughtOfTheDay = _user.thoughtOfTheDay(thoughtOfTheDay);

		try {
			storeMainProperty("thoughtOfTheDay", thoughtOfTheDay);
		} catch (IOException e) {
			_user.lamentException(e);
			return;
		}
		_life.thoughtOfTheDay(thoughtOfTheDay);
	}

	private LifeView remoteContact(String tcpAddress) {
		String[] addressParts = tcpAddress.split(":");
		String ipAddress = addressParts[0];
		int port = DEFAULT_PORT;
		if (addressParts.length > 1) {
			port = Integer.parseInt(addressParts[1]);
		}
		Connection connection = new Connection(_network, ipAddress, port); //TODO: Refactor this. Consider hiding Connection inside LifeViewProxy.
		return connection.lifeView();
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