package sneer.kernel.appmanager;

import sneer.kernel.gui.contacts.ContactAction;

public interface App {
	
	public String name();
	
	public ContactAction contactAction();
	
	public int priority();
	
	public String channelName();

}
