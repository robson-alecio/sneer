package sneer.kernel.appmanager;

import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User;
import wheel.reactive.lists.ListSignal;

public class AppConfig {
	
	public final AppChannelFactory _channelFactory;
	public final User _user;
	public final ListSignal<Contact> _contacts;
	public final ListSignal<SovereignApplicationUID> _publishedApps; //Fix: security breach. apps will be capable to access other apps directly

	public AppConfig(User user, AppChannelFactory channelFactory, ListSignal<Contact> contacts, ListSignal<SovereignApplicationUID> publishedApps){
		_user = user;
		_channelFactory = channelFactory;
		_contacts = contacts;
		_publishedApps = publishedApps;
	}

}
