package sneer.kernel.appmanager;

import java.util.List;

import sneer.kernel.gui.contacts.ContactAction;
import wheel.io.ui.Action;

public interface SovereignApplication {
	
	public List<Action> mainActions();
	public List<ContactAction> contactActions();

	public void start(AppConfig config);

}
