package sneer.apps.filetransfer;


import java.util.List;

import sneer.kernel.api.SovereignApplicationAbstractImpl;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.gui.contacts.DropAction;

public class Application extends SovereignApplicationAbstractImpl{

	private FileTransferApp _delegate;
	
	@Override
	public String defaultName() {
		return "FileTransfer";
	}

	@Override
	public int trafficPriority() {
		return 3;
	}
	
	@Override
	public void start() {
		_delegate = new FileTransferApp(_config);
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
