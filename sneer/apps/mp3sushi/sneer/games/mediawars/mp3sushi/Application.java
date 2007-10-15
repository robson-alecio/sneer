package sneer.games.mediawars.mp3sushi;

import java.util.Collections;
import java.util.List;

import sneer.kernel.api.SovereignApplicationNeeds;
import sneer.kernel.api.SovereignApplication;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.gui.contacts.DropAction;
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
	public void start(SovereignApplicationNeeds config) {
		_delegate = new MP3SushiGameApp(config);
	}

	@Override
	public List<ContactAction> contactActions() {
		return Casts.uncheckedGenericCast(Collections.EMPTY_LIST);
	}
	
	@Override
	public List<DropAction> dropActions() {
		return Casts.uncheckedGenericCast(Collections.EMPTY_LIST);
	}

	@Override
	public List<Action> mainActions() {
		return _delegate.mainActions();
	}

	@Override
	public Object initialState() {
		return null;
	}
	

}
