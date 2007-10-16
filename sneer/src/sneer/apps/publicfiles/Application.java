package sneer.apps.publicfiles;


import java.util.List;

import sneer.kernel.api.SovereignApplicationAbstractImpl;
import sneer.kernel.gui.contacts.ContactAction;
import wheel.io.ui.Action;

public class Application extends SovereignApplicationAbstractImpl{

	private PublicFiles _delegate;
	
	public Application(){
	}

	@Override
	public String defaultName() {
		return "PublicFiles";
	}

	@Override
	public int trafficPriority() {
		return 2;
	}
	
	@Override
	public void start() {
		_delegate = new PublicFiles(_config.user(),_config.channel(),_config.contacts(),_config.transfer(),_config.contactAttributes());
	}
	
	@Override
	public List<ContactAction> contactActions() {
		return _delegate.contactActions();
	}
	
	@Override
	public List<Action> mainActions() {
		return _delegate.mainActions();
	}

}
