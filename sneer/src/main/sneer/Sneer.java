//Copyright (C) 2005 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.prevayler.foundation.Cool;
import org.prevayler.foundation.network.OldNetwork;
import org.prevayler.foundation.network.OldNetworkImpl;

import sneer.life.Life;
import sneer.life.LifeImpl;
import sneer.life.LifeView;
import sneer.remote.Connection;
import sneer.remote.ParallelServer;

public class Sneer {
	
	static private final int DEFAULT_PORT = 5905;

	static public interface User {
		String confirmName(String currentName);
		int confirmServerPort(int currentPort);
		String thoughtOfDay(String currentThought);
		
		String giveNickname();
		String informTcpAddress(String defaultAddress);

		void lookAtMe();

		void lamentException(Exception e);
		void acknowledge(String fact);
		String writePublicMessage();
	}
	
	private final Life _life;
	private final User _user;
	private final OldNetwork _network = new OldNetworkImpl();
	private Set<String> _knownMessages = new HashSet<String>();

	public Sneer(User user) {
		if (null == user) throw new IllegalArgumentException();
		_user = user;
		_life = new LifeImpl(_user.confirmName("Sneer User"));
		startUserNotificationDaemon();
		startServer();
	}

	private void startServer() {
		try {
			int serverPort = _user.confirmServerPort(DEFAULT_PORT);
			new ParallelServer(_life, _network.openObjectServerSocket(serverPort));
		} catch (IOException e) {
			_user.lamentException(e);
		}
	}

	private void startUserNotificationDaemon() {
		Cool.startDaemon(new Runnable() {
			public void run() {
				while (true) {
					_user.lookAtMe();
					Cool.sleep(1000*10);
				}				
			}
		});
	}
	
	public Life life() {
		return _life;
	}

	public void addContact() {
		String nickname = _user.giveNickname();
		_life.giveSomebodyANickname(remoteContact(), nickname);

		_user.lookAtMe();
	}
	
	public void editPersonalInfo() {
		_life.thoughtOfTheDay(_user.thoughtOfDay(_life.thoughtOfTheDay()));
	}

	private LifeView remoteContact() {
		String tcpAddress = _user.informTcpAddress("localhost:" + DEFAULT_PORT);
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

	private void showMessages(String sender, List<String> messages) {
		for (String message : messages) {
			if (_knownMessages.contains(message)) continue;
			_knownMessages.add(message);
			_user.acknowledge("Message sent from " + sender + ":\n\n" + message);
		}
		
	}

}