package sneer.kernel.api;

import java.util.List;

import sneer.kernel.gui.contacts.ContactAction;
import wheel.io.ui.Action;

public interface SovereignApplication {
	
	public String defaultName();
	
	public int trafficPriority();
	
	public List<Action> mainActions();
	public List<ContactAction> contactActions();

	public Object initialState();
	
	public void start(SovereignApplicationNeeds config);

}
