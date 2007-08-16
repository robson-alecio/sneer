package sneer.kernel.appmanager;

import java.util.List;

import sneer.kernel.gui.contacts.ContactAction;
import wheel.io.ui.Action;

public interface App {
	
	public String name();
	
	public List<ContactAction> contactActions();
	
	public List<Action> mainActions();
	
	public int priority();
	
	public String channelName();

}
