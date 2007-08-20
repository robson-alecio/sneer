package sneer.kernel.appmanager;

import prevayler.bubble.PrevalentBubbleBlower;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User;
import wheel.reactive.lists.ListSignal;

public class AppConfig {
	
	public final AppChannelFactory _channelFactory;
	public final User _user;
	public final ListSignal<Contact> _contacts;
	public final ListSignal<SovereignApplicationUID> _publishedApps; //Fix: security breach. apps will be capable to access other apps directly
	public final PrevalentBubbleBlower _prevalentBubbleBlower;

	public AppConfig(User user, AppChannelFactory channelFactory, ListSignal<Contact> contacts, ListSignal<SovereignApplicationUID> publishedApps, PrevalentBubbleBlower prevalentBubbleBlower) {
		_user = user;
		_channelFactory = channelFactory;
		_contacts = contacts;
		_publishedApps = publishedApps;
		_prevalentBubbleBlower = prevalentBubbleBlower;
	}

}
