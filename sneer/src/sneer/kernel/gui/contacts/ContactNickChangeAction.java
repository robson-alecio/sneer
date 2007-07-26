package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User;
import wheel.io.ui.ValueChangePane;
import wheel.lang.Consumer;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;

public class ContactNickChangeAction implements ContactAction {

	private final User _user;
	private final Consumer<Pair<ContactId, String>> _nickChanger;

	public ContactNickChangeAction(User user, Consumer<Pair<ContactId, String>> nickChanger) {
		_user = user;
		_nickChanger = nickChanger;
	}

	@Override
	public void actUpon(Contact contact) {
		String title = translate("Nickname Change");
		String prompt = translate("Enter the new nickname for this contact:");
		new ValueChangePane(title, prompt, _user, contact.nick(), nickChangerFor(contact.id())).run();
	}

	private Consumer<String> nickChangerFor(final ContactId id) {
		return new Consumer<String>() { @Override public void consume(String newNick) throws IllegalParameter {
			_nickChanger.consume(new Pair<ContactId, String>(id, newNick));
		}};
	}

	public String caption() {
		return translate("Change Nickname");
	}

}
