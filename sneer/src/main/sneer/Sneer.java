package sneer;

import java.io.IOException;
import java.lang.reflect.Proxy;

import org.prevayler.foundation.Cool;
import org.prevayler.foundation.network.OldNetwork;
import org.prevayler.foundation.network.OldNetworkImpl;

import sneer.remote.ConnectionStatus;
import sneer.remote.RemoteLife;

public class Sneer {
	
	public interface User {
		String name();

		String giveNickname();

		String informTcpAddress();

		void lamentException(IOException e);

		void lookAtMe();
	}
	
	private final Life _life;
	private final User _user;
	private final OldNetwork _network = new OldNetworkImpl();

	public Sneer(User user) {
		if (null == user) throw new IllegalArgumentException();
		_user = user;
		_life = new LifeImpl(_user.name());
		Cool.startDaemon(new Runnable() {
			public void run() {
				while (true) {
					_user.lookAtMe();
					Cool.sleep(500);
				}				
			}
		});
	}
	
	public Life life() {
		return _life;
	}

	public void addContact() {
		String nickname = _user.giveNickname();
		String tcpAddress = _user.informTcpAddress();
		String[] addressParts = tcpAddress.split(":");
		String ipAddress = addressParts[0];
		int port = 5901;
		if (addressParts.length > 1) {
			port = Integer.parseInt(addressParts[1]);
		}
		LifeView contact = RemoteLife.createWith("ignored", _network, ipAddress, port);
		_life.giveSomebodyANickname(contact, nickname);
		_user.lookAtMe();
	}

	public ConnectionStatus connectionStatus(String nickname) {
		return ((RemoteLife)Proxy.getInvocationHandler(_life.contact(nickname))).connectionStatus();
	}
}