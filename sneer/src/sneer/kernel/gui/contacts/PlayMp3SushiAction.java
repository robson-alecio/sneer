package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;
import sneer.games.mediawars.mp3sushi.MP3SushiGameApp;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.communication.Channel;
import wheel.io.ui.User;
import wheel.io.ui.TrayIcon.Action;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public class PlayMp3SushiAction implements Action {

	private Channel _channel;
	private ListSignal<ContactAttributes> _contactsAttributes;
	private MP3SushiGameApp _mp3SushiGameApp;
	private User _user;
	private Signal<String> _ownName;
	
	public PlayMp3SushiAction(Signal<String> ownName, User user, Channel channel, ListSignal<ContactAttributes> contactsAttributes) {
		_user = user;
		_channel = channel;
		_contactsAttributes = contactsAttributes;
		_ownName = ownName;
	}
	
	@Override
	public String caption() {
		// Implement Auto-generated method stub
		return translate("MP3 Sushi");
	}

	@Override
	public void run() {
		if (_mp3SushiGameApp == null)
			_mp3SushiGameApp = new MP3SushiGameApp(_ownName, _user, _channel, _contactsAttributes);
		_mp3SushiGameApp.start();
	}

}
