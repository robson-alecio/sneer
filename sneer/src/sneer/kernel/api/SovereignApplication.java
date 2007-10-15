package sneer.kernel.api;

import java.util.List;

import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.gui.contacts.DropAction;
import wheel.io.ui.Action;

//Refactor: create a similar class for system apps. to avoid current system apps API mess.
public interface SovereignApplication {
	
	public String defaultName();
	
	public int trafficPriority();
	
	public List<Action> mainActions();
	public List<ContactAction> contactActions();
	public List<DropAction> dropActions();

	public Object initialState();
	
	public void start(SovereignApplicationNeeds config);

}
