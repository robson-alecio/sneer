package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMap;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMaps;
import sneer.bricks.network.social.Contact;
import sneer.foundation.lang.Producer;

class ConnectionsByContact {

	static private final CacheMap<Contact, ByteConnectionImpl> _cache = my(CacheMaps.class).newInstance();


	static ByteConnectionImpl get(final Contact contact) {
		return _cache.get(contact, new Producer<ByteConnectionImpl>() { @Override public ByteConnectionImpl produce() {
			return new ByteConnectionImpl();
		}});
	}


	static ByteConnectionImpl remove(Contact contact) {
		return _cache.remove(contact);
	}


	static Iterable<ByteConnectionImpl> all() {
		return _cache.values();
	}

}
