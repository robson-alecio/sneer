package sneer.kernel.communication;

import java.io.IOException;

import sneer.kernel.business.chat.ChatEvent;

import wheel.io.Log;
import wheel.io.network.OldNetwork;
import wheel.io.ui.User;
import wheel.lang.Consumer;
import wheel.lang.Threads;
import wheel.lang.exceptions.FriendlyException;
import wheel.reactive.Receiver;
import wheel.reactive.Signal;

class ServerStarter implements Receiver<Integer> {
	
	private final User _user;
	private final OldNetwork _network;
	private volatile Server _server;
	private final Consumer<ChatEvent> _chatSender;

	public static void start(User user, OldNetwork network, Signal<Integer> sneerPort, Consumer<ChatEvent> chatSender) {
		new ServerStarter(user, network, sneerPort, chatSender);
	}

	private ServerStarter(User user, OldNetwork network, Signal<Integer> sneerPort, Consumer<ChatEvent> chatSender) {
		_user = user;
		_network = network;
		_chatSender = chatSender;

		sneerPort.addTransientReceiver(this);
		Threads.preventFromBeingGarbageCollected(this);
	}

	@Override
	public void receive(Integer newPort) {
		if (_server != null) _server.stop();
		
		try {
			_server = Server.start(_network, newPort, _chatSender);
		} catch (IOException e) {
			Log.log(e);
		} catch (FriendlyException e) {
			_user.acknowledgeFriendlyException(e);
		}
	}

}