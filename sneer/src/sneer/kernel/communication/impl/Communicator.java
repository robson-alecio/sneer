package sneer.kernel.communication.impl;


import java.util.HashMap;
import java.util.Map;

import sneer.kernel.business.Business;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import wheel.io.network.OldNetwork;
import wheel.io.ui.User;
import wheel.lang.Omnivore;

public class Communicator {

	private Map<String, Channel> _channelsById = new HashMap<String, Channel>();
	private Spider _spider;

	public Communicator(User user, OldNetwork network, BusinessSource businessSource) {
		Business business = businessSource.output();
		
		new ServerStarter(user, network, business.sneerPort());
		_spider = new Spider(network, business.contacts(), businessSource.contactOnlineSetter());
	}

	public Channel getChannel(String channelId) {
		Channel result = _channelsById.get(channelId);
		if (result != null) return result;
		
		ChannelImpl newChannel = new ChannelImpl(channelId);
		_channelsById.put(channelId, newChannel);
		return newChannel;
		
	}


}
