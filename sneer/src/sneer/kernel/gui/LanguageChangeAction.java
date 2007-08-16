package sneer.kernel.gui;

import static wheel.i18n.Language.translate;
import wheel.io.ui.Action;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

class LanguageChangeAction implements Action {

	LanguageChangeAction(User user, Signal<String> language, Omnivore<String> languageSetter) {
		_user = user;
		_language = language;
		_languageSetter = languageSetter;
	}

	private final User _user;
	private final Signal<String> _language;
	private final Omnivore<String> _languageSetter;
	
	public String caption() {
		return translate("Language");
	}

	public void run() {
		Object[] options = { "English", "Português" }; //Implement: Detect available languages. Disable current language button
		String button = null;
		try {
			button = (String)_user.choose(translate("Available Languages:"),options);
		} catch (CancelledByUser ignored) {
			return;
		}
			
		String language = button.equals("Português")
			? "pt_BR"
			: "en";
			
		String current = _language.currentValue();
		if (language.equals(current)) return;
		_languageSetter.consume(language);
		_user.acknowledgeNotification("Your changes will take effect next time you run Sneer.");
	}
}