package sneer.games.mediawars.mp3sushi;

import java.util.Collections;
import java.util.List;

import sneer.kernel.appmanager.AppConfig;
import sneer.kernel.appmanager.SovereignApplication;
import sneer.kernel.gui.contacts.ContactAction;
import wheel.io.ui.Action;
import wheel.lang.Casts;

public class Application implements SovereignApplication{

	private MP3SushiGameApp _delegate;

	@Override
	public String defaultName() {
		return "MP3 Sushi";
	}

	@Override
	public int trafficPriority() {
		return 2;
	}

	@Override
	public void start(AppConfig config) {
		_delegate = new MP3SushiGameApp(config);
	}

	@Override
	public List<ContactAction> contactActions() {
		return Casts.uncheckedGenericCast(Collections.EMPTY_LIST);
	}

	@Override
	public List<Action> mainActions() {
		return _delegate.mainActions();
	}
	

}
