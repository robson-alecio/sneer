package sneer.kernel.appmanager;

import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.Communicator;

public class AppChannelFactory {

	private final Communicator _communicator;


	public AppChannelFactory(Communicator communicator){
		_communicator = communicator;
	}
	
	public Channel channel(SovereignApplication app){ //Implement: in the future the priority should be subordinated to some permission system
		return _communicator.getChannel(app.defaultName(), app.trafficPriority());
	}
	
}
