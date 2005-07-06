package sneer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.prevayler.foundation.Cool;
import org.prevayler.foundation.network.OldNetwork;
import org.prevayler.foundation.network.OldNetworkImpl;

import sneer.life.Life;
import sneer.life.LifeImpl;
import sneer.remote.*;

public class Sneer {
	
	public interface User {
		String name();

		String giveNickname();

		String informTcpAddress();

		void lamentException(IOException e);

		void lookAtMe();

		String thoughtOfDay(String current);
	}
	
	private final Life _life;
	private final User _user;
	private final OldNetwork _network = new OldNetworkImpl();
	private final Map<String, Connection> _connectionsByNickname = new HashMap<String, Connection>();

	public Sneer(User user) {
		if (null == user) throw new IllegalArgumentException();
		_user = user;
		_life = new LifeImpl(_user.name());
		startUserNotificationDaemon();
		startServer();
	}

	private void startServer() {
		try {
			new Server(_life, _network.openObjectServerSocket(5909));
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
		
		Connection connection = createConnection(nickname);
		_connectionsByNickname.put(nickname, connection);
		
		_life.giveSomebodyANickname(connection.lifeView(), nickname);
		
		_user.lookAtMe();
	}
	
	public void editPersonalInfo() {
		_life.thoughtOfTheDay(_user.thoughtOfDay(_life.thoughtOfTheDay()));
	}

	private Connection createConnection(String nickname) {
		String tcpAddress = _user.informTcpAddress();
		String[] addressParts = tcpAddress.split(":");
		String ipAddress = addressParts[0];
		int port = 5901;
		if (addressParts.length > 1) {
			port = Integer.parseInt(addressParts[1]);
		}
		Connection result = new Connection(_network, ipAddress, port);
		return result;
	}

	public boolean isOnline(String nickname) {
		return _connectionsByNickname.get(nickname).isOnline();
	}
}