package sneer.pulp.connection.impl;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.connection.SocketAccepter;
import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventNotifiers;
import sneer.pulp.events.EventSource;
import sneer.pulp.log.Logger;
import sneer.pulp.network.ByteArrayServerSocket;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;
import sneer.pulp.port.PortKeeper;
import sneer.pulp.reactive.Signals;
import sneer.pulp.threads.Threads;

class SocketAccepterImpl implements SocketAccepter {
	
	private final PortKeeper _portKeeper = my(PortKeeper.class);
	private final Network _network = my(Network.class);
	private final BlinkingLights _lights = my(BlinkingLights.class);
	private final Threads _threads = my(Threads.class);

	private final EventNotifier<ByteArraySocket> _notifier = my(EventNotifiers.class).create();

	private final transient Object _portToListenMonitor = new Object();

	private ByteArrayServerSocket _serverSocket;
	
	private volatile boolean _isStopped;

	private int _portToListen;
	
	private Light _cantOpenServerSocket = _lights.prepare(LightType.ERROR);

	private final Light _cantAcceptSocket = _lights.prepare(LightType.ERROR);

	SocketAccepterImpl() {
		my(Signals.class).receive(this, new Consumer<Integer>() { @Override public void consume(Integer port) {
			setPort(port);
		}}, _portKeeper.port());
		_threads.registerActor(new Runnable(){ @Override public void run() {
			listenToSneerPort();
		}});
	}

	@Override
    public EventSource<ByteArraySocket> lastAcceptedSocket() {
    	return _notifier.output();
    }

    private void setPort(int port) {
		synchronized (_portToListenMonitor) {
			_portToListen = port;
			_portToListenMonitor.notify();
		}
	}

    private void listenToSneerPort() {
    	while (true) {
    		int myPortToListen = _portToListen;
    		crashServerSocketIfNecessary();
    		openServerSocket(myPortToListen);	
    		if(_serverSocket != null) startAccepting();
    		
    		synchronized (_portToListenMonitor) {
    			if (myPortToListen == _portToListen)
    				_threads.waitWithoutInterruptions(_portToListenMonitor);
    		}
    	}
    }
	
	private void startAccepting() {
		_isStopped = false;
		_threads.registerActor(new Runnable() { @Override public void run() {
			while (!_isStopped) {
				try {
					dealWith(_serverSocket.accept());
				} catch (IOException e) {
					dealWith(e);
				} 
			}
		}});

	}
	
	private void dealWith(ByteArraySocket incomingSocket) {
		_lights.turnOffIfNecessary(_cantAcceptSocket);
		my(Logger.class).log("Incoming socket received");
		_notifier.notifyReceivers(incomingSocket);
	}

	private void dealWith(IOException e) {
		if (!_isStopped) 
			_lights.turnOnIfNecessary(_cantAcceptSocket, "Unable to accept client connection", null, e);
	}

	private void openServerSocket(int port) {
		if (port == 0) return;
		try {
			_serverSocket = _network.openServerSocket(port);
			_lights.turnOffIfNecessary(_cantOpenServerSocket);
			_lights.turnOn(LightType.GOOD_NEWS, "TCP port opened: " + port, "Sneer has successfully opened TCP port " + port + " to receive incoming connections from others.", 7000);
		} catch (IOException e) {
			if (!_isStopped)
				_lights.turnOnIfNecessary(_cantOpenServerSocket, "Unable to listen on TCP port " + port, helpMessage(), e);
		}
	}

	private String helpMessage() {
		return "Typical causes:\n" +
			"- You might have another Sneer instance already running\n" +
			"- Some other application is already using that port\n" +
			"- Your operating system or firewall is blocking that port, especially if it is below 1024\n" +
			"\n" +
			"You can run multiple Sneer instances on the same machine but each has to use a separate TCP port.";
	}

	private void crashServerSocketIfNecessary() {
		if(_serverSocket == null) return;

		my(Logger.class).log("crashing server socket");
		_isStopped = true;
		_serverSocket.crash();
	}
}
