package sneer.old;

import java.io.Serializable;

import sneer.old.life.Life;
import sneer.old.life.LifeImpl;
import sneer.old.life.LifeView;
import sneer.old.remote.Connection;
import wheel.io.network.OldNetwork;

public class Home implements Serializable {

	public static final int DEFAULT_PORT = 7007;

	private Life _life;
	private int _serverPort;  //TODO Refactoring - Move this to Life.thing("sneer.configuration.serverport") and eliminate the need for persisting this Home class.

	public static OldNetwork _network;

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
	
	void addContact(String nickname, String ipAddress, int port) {
		_life.giveSomebodyANickname(remoteContact(ipAddress, port), nickname);
	}

	private LifeView remoteContact(String ipAddress, int port) {
		Connection connection = new Connection(_network, ipAddress, port, _life.name()); //TODO: Refactor this. Consider hiding Connection inside LifeViewProxy.
		return connection.lifeView();
	}
	
	private static final long serialVersionUID = 1L;

	public void removeContact(String nickname) {
		_life.forgetNickname(nickname);
	}

}
