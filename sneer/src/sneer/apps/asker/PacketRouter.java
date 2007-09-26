package sneer.apps.asker;

import java.util.Hashtable;
import java.util.Map;

import sneer.kernel.communication.Packet;
import wheel.lang.Omnivore;

public class PacketRouter implements Omnivore<Packet>{
	
	private Map<Class<?>,Omnivore<Packet>> _clazzToCallback = new Hashtable<Class<?>,Omnivore<Packet>>();
	private Omnivore<Packet> _unrecognizedPackets;
	
	public PacketRouter(Omnivore<Packet> unrecognizedPackets){
		_unrecognizedPackets = unrecognizedPackets;
	}
	
	public void register(Class<?> clazz, Omnivore<Packet> callback){
		_clazzToCallback.put(clazz, callback);
	}
	
	public void consume(Packet packet){
		if (packet._contents != null)
			for(Class<?> clazz:_clazzToCallback.keySet())
				if (clazz.isInstance(packet._contents)){
					_clazzToCallback.get(clazz).consume(packet);
					return;
				}
		if (_unrecognizedPackets != null)
			_unrecognizedPackets.consume(packet);
	}
	
}
