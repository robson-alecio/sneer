package sneer;

import java.io.IOException;

import org.prevayler.foundation.network.ObjectSocket;
import org.prevayler.foundation.network.OldNetwork;
import org.prevayler.foundation.network.OldNetworkImpl;

import sneer.remote.RemoteLife;

public class Sneer {
	
	public interface User {
		String name();

		String giveNickname();

		String informTcpAddress();

		void lamentException(IOException e);

		void checkOutNewContacts();
	}
	
	private final Life _life;
	private final User _user;
	private final OldNetwork _network = new OldNetworkImpl();

	public Sneer(User user) {
		if (null == user) throw new IllegalArgumentException();
		_user = user;
		_life = new LifeImpl(_user.name());
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
		ObjectSocket objectSocket;
		try {
			objectSocket = _network.openSocket(ipAddress, port);
		} catch (IOException e) {
			_user.lamentException(e);
			return;
		}
		LifeView contact = RemoteLife.createWith("ignored", objectSocket);
		_life.giveSomebodyANickname(contact, nickname);
		_user.checkOutNewContacts();
	}
}