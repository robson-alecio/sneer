package sneer;

import java.io.Serializable;

import org.prevayler.foundation.network.OldNetwork;

import sneer.life.Life;
import sneer.life.LifeImpl;
import sneer.life.LifeView;
import sneer.remote.Connection;

public class Home implements Serializable {

	public static final int DEFAULT_PORT = 7007;

	private Life _life;
	private int _serverPort;

	static OldNetwork _network;

	public Life life() {
		return _life;
	}

	public void foster(String name, int serverPort) {
		assert _life != null;

		_life = new LifeImpl(name);
		_serverPort = serverPort;
	}

	public int serverPort() {
		return _serverPort;
	}
	
	void addContact(String nickname, String tcpAddress) {
		_life.giveSomebodyANickname(remoteContact(tcpAddress), nickname);
	}

	private LifeView remoteContact(String tcpAddress) {
		String[] addressParts = tcpAddress.split(":");
		String ipAddress = addressParts[0];
		int port = Home.DEFAULT_PORT;
		if (addressParts.length > 1) {
			port = Integer.parseInt(addressParts[1]);
		}
		Connection connection = new Connection(_network, ipAddress, port); //TODO: Refactor this. Consider hiding Connection inside LifeViewProxy.
		return connection.lifeView();
	}
	
	private static final long serialVersionUID = 1L;


}
