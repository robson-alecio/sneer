package sneer.apps.conversations;

import sneer.apps.conversations.gui.ConversationScreen;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Connection;
import sneer.kernel.communication.Channel;
import sneer.kernel.gui.contacts.ContactAction;
import wheel.lang.exceptions.NotImplementedYet;

public class ConversationsApp {

	public ConversationsApp(Channel operator) {
		if (operator == null)
			throw new IllegalArgumentException();
		_operator = operator;
	}

	private final Channel _operator;

	public ContactAction contactAction() {
		return new ContactAction(){

			@Override
			public void actUpon(Contact contact) {
				Connection connection = _operator.connectionTo(contact.id());
				new ConversationScreen(contact.nick(), connection.input(), connection.output());
			}

			@Override
			public String caption() {
				return "Start Conversation";
			}
			
		};
	}

}
