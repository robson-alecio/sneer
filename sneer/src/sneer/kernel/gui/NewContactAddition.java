package sneer.kernel.gui;

import java.util.Date;
import static wheel.i18n.Language.*;

import org.prevayler.Transaction;

import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactInfo;

import wheel.io.network.PortNumberSource;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.io.ui.ValueChangePane;
import wheel.lang.Consumer;
import wheel.lang.IntegerParser;
import wheel.lang.exceptions.IllegalParameter;
import wheel.lang.exceptions.NotImplementedYet;

public class NewContactAddition {

	public NewContactAddition(User user, Consumer<ContactInfo> contactAdder) throws CancelledByUser {
		while (true) {
			String nick = user.answer(translate("New contact's nickname"));
			String host = user.answer(translate("Host Address for %1$s",nick), nick + ".dyndns.org");

			int port = port(user, nick);
			
			try {
				contactAdder.consume(new ContactInfo(nick, host, port, "", ContactAttributes.UNCONFIRMED_STATE));
				return;
			} catch (IllegalParameter e) {
				user.acknowledgeNotification(e.getMessage());
			}
		}
	}

	private int port(User user, String nick) throws CancelledByUser {
		PortNumberSource result = new PortNumberSource(0);
		new ValueChangePane(translate("Port Number"), translate("Sneer Port for %1$s",nick), user, result.output(), new IntegerParser(result.setter())).tryToRun();
		return result.output().currentValue();
	}

	private static final long serialVersionUID = 1L;
}
