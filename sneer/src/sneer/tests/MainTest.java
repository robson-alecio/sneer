package sneer.tests;

import java.io.Serializable;

import org.prevayler.Prevayler;

import sneer.SneerImpl;
import sneer.SneerImpl.Context;
import wheel.io.ui.TrayIcon;
import wheel.io.ui.User;
import wheel.io.ui.tests.TestUser;

public class MainTest implements Context {

	public static void main(String[] args) {
		new MainTest();
	}

	public MainTest() {
		new SneerImpl(this);
	}

	public Prevayler prevaylerFor(Serializable rootObject) throws Exception {
		
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
