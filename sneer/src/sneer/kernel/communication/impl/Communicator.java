package sneer.kernel.communication.impl;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import sneer.kernel.business.Business;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.ChannelImpl.MuxProvider;
import wheel.io.Connection;
import wheel.io.Log;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.lang.exceptions.IllegalParameter;

public class Communicator {

	public Communicator(User user, OldNetwork network, BusinessSource businessSource) {
		Business business = businessSource.output();
		
		prepareBusiness(businessSource);
		
		new SocketAccepter(user, network, business.sneerPort(), mySocketServer());
		_spider = new Spider(business.publicKey().currentValue(), network, business.contacts(), businessSource.contactOnlineSetter());
	}

	private Spider _spider;
	private Map<String, Channel> _channelsById = new HashMap<String, Channel>();
	private Map<ContactId, Mux> _muxesByContactId = new HashMap<ContactId, Mux>();

	
	private String prepareBusiness(BusinessSource businessSource) {
		int sneerPort = businessSource.output().sneerPort().currentValue();
		if (sneerPort == 0) initSneerPort(businessSource);

		String id = businessSource.output().publicKey().currentValue();
		System.out.println("id: " + id);
		if (id.isEmpty()) initId(businessSource);
		return id;
	}


	public Channel getChannel(String channelId) {
		Channel result = _channelsById.get(channelId);
		if (result != null) return result;
		
		result = new ChannelImpl(channelId, myMuxProvider());
		_channelsById.put(channelId, result);
		return result;
	}

	private MuxProvider myMuxProvider() {
		return new MuxProvider() {
			public Mux muxFor(ContactId contactId) {
				Mux result = _muxesByContactId.get(contactId);
				if (result != null) return result;
				
				result = new Mux(connectionFor(contactId));
				_muxesByContactId.put(contactId, result);
				return result;
			}
		};
	}

	private Connection connectionFor(ContactId contactId) {
		return _spider.connectionFor(contactId);
	}

	private void initId(BusinessSource businessSource) {
		String id = "" + System.currentTimeMillis() + "/" + System.nanoTime();
		businessSource.publicKeySetter().consume(id);
	}

	private void initSneerPort(BusinessSource businessSource) {
		int randomPort = 10000 + new Random().nextInt(50000);
		try {
			businessSource.sneerPortSetter().consume(randomPort);
		} catch (IllegalParameter e) {
			throw new IllegalStateException();
		}
	}

	private Omnivore<ObjectSocket> mySocketServer() {
		return new Omnivore<ObjectSocket>() { public void consume(ObjectSocket socket) {
			serve(socket);
		} };
	}

	private void serve(final ObjectSocket socket) {
		Threads.startDaemon(new Runnable() { @Override public void run() {
			while (true){
				try {
					Object readObject = socket.readObject();
					System.out.println("Received: " + readObject);
				} catch (IOException e) {
					// Implement This is the moment where a disconnection occurs. Check to see whether the online watchdog ("Bark") is necessary.
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					Log.log(e);
				} 
			}
		}});
	}

}
