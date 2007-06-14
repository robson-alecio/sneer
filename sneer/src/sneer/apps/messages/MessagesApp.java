package sneer.apps.messages;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.gui.contacts.ContactAction;
import wheel.lang.exceptions.NotImplementedYet;

public class MessagesApp {

	public ContactAction contactAction() {
		return new ContactAction(){

			@Override
			public void actUpon(Contact contact) {
				throw new NotImplementedYet();
			}

			@Override
			public String caption() {
				return "Send Message";
			}
			
		};
	}

}
