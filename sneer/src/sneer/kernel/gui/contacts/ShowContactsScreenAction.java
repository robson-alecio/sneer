package sneer.kernel.gui.contacts;

import sneer.kernel.business.Business;
import wheel.io.ui.TrayIcon.Action;

public class ShowContactsScreenAction implements Action {

	private final Business _business;
	public ShowContactsScreenAction(Business business){
		_business = business;		
	}

	public String caption() {
		return "Show contacts screen";
	}

	public void run() {
		new FriendsScreen(new FriendsModelImpl(_business));
	}

}
