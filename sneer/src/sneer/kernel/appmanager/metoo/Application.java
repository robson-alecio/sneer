package sneer.kernel.appmanager.metoo;


import java.util.List;

import sneer.kernel.api.SovereignApplicationAbstractImpl;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.gui.contacts.ContactAction;

public class Application extends SovereignApplicationAbstractImpl{

	private MeToo _delegate;
	private AppManager _appManager;
	
	public Application(AppManager appManager){
		_appManager = appManager;
	}

	@Override
	public String defaultName() {
		return "Metoo";
	}

	@Override
	public int trafficPriority() {
		return 2;
	}
	
	@Override
	public void start() {
		_delegate = new MeToo(_config.user(),_config.channel(), _appManager, _config.transfer());
	}

	@Override
	public List<ContactAction> contactActions() {
		return _delegate.contactActions();
	}

}
