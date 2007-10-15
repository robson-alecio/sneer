package sneer.apps.sharedfolder;


import java.util.List;

import sneer.kernel.api.SovereignApplicationAbstractImpl;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.gui.contacts.DropAction;

public class Application extends SovereignApplicationAbstractImpl{

	private SharedFolder _delegate;
	
	public Application(){
	}

	@Override
	public String defaultName() {
		return "TransferQueue";
	}

	@Override
	public int trafficPriority() {
		return 2;
	}
	
	@Override
	public void start() {
		_delegate = new SharedFolder(_config.channel(),_config.contacts(),_config.transfer());
	}
	
	@Override
	public List<ContactAction> contactActions() {
		return _delegate.contactActions();
	}
	
	@Override
	public List<DropAction> dropActions() {
		return _delegate.dropActions();
	}

}
