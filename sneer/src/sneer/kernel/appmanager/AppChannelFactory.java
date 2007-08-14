package sneer.kernel.appmanager;

import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.Communicator;

public class AppChannelFactory {

	private final Communicator _communicator;
	private final int _priority;


	public AppChannelFactory(Communicator communicator, int priority){
		_communicator = communicator;
		_priority = priority;
		
	}
	
	public Channel channel(String channelName){
		return _communicator.getChannel(channelName, _priority);
	}
	
}
