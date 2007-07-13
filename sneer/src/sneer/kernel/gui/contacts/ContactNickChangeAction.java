package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;
import sneer.kernel.business.contacts.Contact;
import wheel.io.ui.Util;
import wheel.io.ui.ValueChangePane;

public class ContactNickChangeAction implements ContactAction {

	public ContactNickChangeAction() {}

	public void actUpon(Contact contact) {
		//return new ValueChangePane(translate("Nickname Change"),Util.correctSwingNewlineSpaceProblem(prompt), _user, _businessSource.output().ownName(), _businessSource.ownNameSetter());
	}

	public String caption() {
		return translate("Change Nickname");
	}

}
