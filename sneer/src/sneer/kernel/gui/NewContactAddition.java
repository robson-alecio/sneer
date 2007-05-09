package sneer.kernel.gui;

import java.util.Date;

import org.prevayler.Transaction;

import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.ContactInfo;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.exceptions.IllegalParameter;
import wheel.lang.exceptions.NotImplementedYet;

public class NewContactAddition {

	public NewContactAddition(User user, BusinessSource business) throws CancelledByUser {
		while(true){
			String nick = user.answer("New contact's nickname");
			String host = user.answer("Host Address for " + nick, nick + ".dyndns.org");
			int port = port(user, nick);
			
			try {
				business.contactAdder().consume(new ContactInfo(nick, host, port));
				return;
			} catch (IllegalParameter e) {
				user.acknowledgeNotification(e.getMessage());
			}
		}
	}

	private int port(User user, String nick) throws CancelledByUser {
		String answer = user.answer("Sneer Port Number for " + nick);
		return Integer.parseInt(answer);
		//Fix: Deal with parse errors. Use same logic as own SneerPortChange action.
	}

	private static final long serialVersionUID = 1L;
}
