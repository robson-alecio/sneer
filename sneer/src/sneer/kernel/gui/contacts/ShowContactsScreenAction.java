package sneer.kernel.gui.contacts;

import sneer.kernel.business.BusinessSource;
import wheel.io.ui.TrayIcon.Action;

public class ShowContactsScreenAction implements Action {

	private final BusinessSource _business;
	public ShowContactsScreenAction(BusinessSource business){
		_business = business;		
	}

	public String caption() {
		return "Show contacts screen";
	}

	public void run() {
		new FriendsScreen(new FriendsModelImpl(_business));
	}

}
