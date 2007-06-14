package sneer.apps.conversations;

import sneer.apps.conversations.gui.ConversationScreen;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.communication.Connection;
import sneer.kernel.communication.Operator;
import sneer.kernel.gui.contacts.ContactAction;
import wheel.lang.exceptions.NotImplementedYet;

public class ConversationsApp {

	public ConversationsApp(Operator operator) {
		_operator = operator;
	}

	private final Operator _operator;

	public ContactAction contactAction() {
		return new ContactAction(){

			@Override
			public void actUpon(Contact contact) {
				Connection<Message> connection = _operator.establishConnectionTo(contact);
				new ConversationScreen(contact.nick(), connection.input(), connection.output());
			}

			@Override
			public String caption() {
				return "Start Conversation";
			}
			
		};
	}

}
