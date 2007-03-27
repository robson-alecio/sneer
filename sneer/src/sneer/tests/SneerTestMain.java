package sneer.tests;

import org.prevayler.Prevayler;

import sneer.Sneer;
import sneer.Sneer.Context;
import sneer.kernel.Domain;
import wheel.io.ui.TrayIcon;
import wheel.io.ui.User;
import wheel.io.ui.tests.TestUser;

public class SneerTestMain implements Context {

	public static void main(String[] args) {
		new SneerTestMain();
	}

	public SneerTestMain() {
		new Sneer(this);
	}

	public Prevayler prevaylerFor(Domain domain) throws Exception {
		
		return null;
	}

	public TrayIcon trayIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	public User user() {
		return new TestUser();
	}

}
