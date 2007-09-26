package sneer.kernel.communication.impl;

import sneer.kernel.communication.Packet;
import wheel.lang.Omnivore;

public class PacketRouter extends Router<Packet>{

	public PacketRouter(Omnivore<Packet> unrecognized) {
		super(unrecognized);
	}
	
	@Override
	public boolean accept(Class<?> clazz, Packet packet){
		return clazz.isInstance(packet._contents);
	}
}
