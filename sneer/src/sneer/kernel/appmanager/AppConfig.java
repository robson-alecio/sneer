package sneer.kernel.appmanager;

import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User;
import wheel.reactive.lists.ListSignal;

public class AppConfig {
	
	public final AppChannelFactory _channelFactory;
	public final User _user;
	public final ListSignal<Contact> _contacts;

	public AppConfig(User user, AppChannelFactory channelFactory, ListSignal<Contact> contacts){
		_user = user;
		_channelFactory = channelFactory;
		_contacts = contacts;
	}

}
