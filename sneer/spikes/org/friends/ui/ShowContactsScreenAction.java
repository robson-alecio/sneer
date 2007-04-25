package org.friends.ui;

import sneer.kernel.business.Business;
import wheel.io.ui.TrayIcon.Action;

public class ShowContactsScreenAction implements Action {

	private final Business _business;
	public ShowContactsScreenAction(Business business){
		_business = business;		
	}

	@Override
	public String caption() {
		return "Show contacts screen";
	}

	@Override
	public void run() {
		new FriendsScreen(new FriendsModelImpl(_business));
	}

}
