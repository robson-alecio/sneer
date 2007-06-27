package sneer.kernel.gui;

import java.util.Date;

import org.prevayler.Transaction;

import static sneer.Language.*;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.contacts.ContactInfo;

import wheel.io.network.PortNumberSource;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.io.ui.impl.ValueChangePane;
import wheel.lang.Consumer;
import wheel.lang.IntegerParser;
import wheel.lang.exceptions.IllegalParameter;
import wheel.lang.exceptions.NotImplementedYet;

public class NewContactAddition {

	public NewContactAddition(User user, Consumer<ContactInfo> contactAdder) throws CancelledByUser {
		while (true) {
			String nick = user.answer(string("NEWCONTACT_PROMPT_NICK"));
			String host = user.answer(string("NEWCONTACT_PROMPT_HOST",nick), nick + ".dyndns.org");
			int port = port(user, nick);
			
			try {
				contactAdder.consume(new ContactInfo(nick, host, port));
				return;
			} catch (IllegalParameter e) {
				user.acknowledgeNotification(e.getMessage());
			}
		}
	}

	private int port(User user, String nick) throws CancelledByUser {

		PortNumberSource result = new PortNumberSource(0);
		new ValueChangePane(string("NEWCONTACT_PORT_PANEL_TITLE"), string("NEWCONTACT_PROMPT_PORT",nick), user, result.output(), new IntegerParser(result.setter())).tryToRun();
		return result.output().currentValue();
	}

	private static final long serialVersionUID = 1L;
}
