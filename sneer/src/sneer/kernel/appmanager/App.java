package sneer.kernel.appmanager;

import java.util.List;

import sneer.kernel.gui.contacts.ContactAction;

public interface App {
	
	public String name();
	
	public List<ContactAction> contactActions();
	
	public List<MainAction> mainActions();
	
	public int priority();
	
	public String channelName();

}
