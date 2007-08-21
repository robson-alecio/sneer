package sneer.kernel.appmanager;

import prevayler.bubble.PrevalentBubbleBlower;
import sneer.kernel.communication.Channel;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User;
import wheel.reactive.lists.ListSignal;

public class AppConfig {
	
	public final Channel _channel;
	public final User _user;
	public final ListSignal<Contact> _contacts;
	public final PrevalentBubbleBlower _prevalentBubbleBlower;

	public AppConfig(User user, Channel channel, ListSignal<Contact> contacts, PrevalentBubbleBlower prevalentBubbleBlower) {
		_user = user;
		_channel = channel;
		_contacts = contacts;
		_prevalentBubbleBlower = prevalentBubbleBlower;
	}

}
