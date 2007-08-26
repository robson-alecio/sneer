package sneer.apps.filetransfer;


import java.util.Collections;
import java.util.List;

import sneer.kernel.appmanager.AppConfig;
import sneer.kernel.appmanager.SovereignApplication;
import sneer.kernel.gui.contacts.ContactAction;
import wheel.io.ui.Action;
import wheel.lang.Casts;

public class Application implements SovereignApplication{

	private FileTransferApp _delegate;

	@Override
	public String defaultName() {
		return "FileTransfer";
	}

	@Override
	public int trafficPriority() {
		return 0;
	}

	@Override
	public void start(AppConfig config) {
		_delegate = new FileTransferApp(config);
	}

	@Override
	public List<ContactAction> contactActions() {
		return _delegate.contactActions();
	}

	@Override
	public List<Action> mainActions() {
		return Casts.uncheckedGenericCast(Collections.EMPTY_LIST);
	}
	

}
