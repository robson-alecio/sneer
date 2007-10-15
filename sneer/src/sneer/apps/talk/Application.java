package sneer.apps.talk;


import java.util.List;

import sneer.kernel.api.SovereignApplicationAbstractImpl;
import sneer.kernel.gui.contacts.ContactAction;

public class Application extends SovereignApplicationAbstractImpl{

	private TalkApp _delegate;

	@Override
	public String defaultName() {
		return "Voice";
	}

	@Override
	public int trafficPriority() {
		return 0;
	}

	@Override
	public void start() {
		_delegate = new TalkApp(_config);
	}

	@Override
	public List<ContactAction> contactActions() {
		return _delegate.contactActions();
	}

}
