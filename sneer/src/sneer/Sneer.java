package sneer;

import java.io.Serializable;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import sneer.SneerImpl.Context;
import wheel.io.ui.TrayIcon;
import wheel.io.ui.User;
import wheel.io.ui.impl.JOptionPaneUser;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.io.ui.impl.TrayIconImpl.SystemTrayNotSupported;

public class Sneer implements Context {
	
	public Sneer() {
		new SneerImpl(this);
	}

	public Prevayler prevaylerFor(Serializable rootObject) throws Exception {
		return PrevaylerFactory.createPrevayler(rootObject, SneerDirectories.prevalenceDirectory().getAbsolutePath());
	}

	public TrayIcon trayIcon() throws SystemTrayNotSupported {
		return new TrayIconImpl(Sneer.class.getResource("/sneer/gui/traymenu/yourIconGoesHere.png"));
	}

	public User user() {
		return new JOptionPaneUser("Sneer");
	}

	public static void main(String[] args) {
		new Sneer();
	}

}
