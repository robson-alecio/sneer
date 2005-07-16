package sneer;

import java.io.IOException;

import org.prevayler.foundation.Cool;
import org.prevayler.foundation.network.OldNetwork;
import org.prevayler.foundation.network.OldNetworkImpl;

import sneer.life.Life;
import sneer.life.LifeImpl;
import sneer.life.LifeView;
import sneer.remote.Connection;
import sneer.remote.ParallelServer;

public class Sneer {
	
	private static final int DEFAULT_PORT = 5905;

	public interface User {
		String confirmName(String currentName);

		String giveNickname();

		String informTcpAddress(String defaultAddress);

		void lamentException(IOException e);

		void lookAtMe();

		String thoughtOfDay(String currentThought);
	}
	
	private final Life _life;
	private final User _user;
	private final OldNetwork _network = new OldNetworkImpl();

	public Sneer(User user) {
		if (null == user) throw new IllegalArgumentException();
		_user = user;
		_life = new LifeImpl(_user.confirmName("Sneer User"));
		startUserNotificationDaemon();
		startServer();
	}

	private void startServer() {
		try {
			new ParallelServer(_life, _network.openObjectServerSocket(DEFAULT_PORT));
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

}