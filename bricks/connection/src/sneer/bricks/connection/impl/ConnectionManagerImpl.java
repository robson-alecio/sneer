package sneer.bricks.connection.impl;

import java.io.IOException;

import sneer.bricks.blinkinglights.BlinkingLights;
import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.network.ByteArrayServerSocket;
import sneer.bricks.network.Network;
import sneer.contacts.Contact;
import sneer.contacts.ContactManager;
import sneer.lego.Brick;
import sneer.lego.Container;
import sneer.lego.Startable;
import sneer.log.Logger;
import wheel.lang.Consumer;
import wheel.lang.IntegerConsumerBoundaries;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class ConnectionManagerImpl implements ConnectionManager, Startable {

	private Object receiver;
	
	private Register<Integer> _sneerPort = new RegisterImpl<Integer>(0);
	
	@Brick
	private Network _network;
	
	private ByteArrayServerSocket _serverSocket;
	
    @Brick
    private Logger _log;

	@Brick
	private ContactManager _contactManager;
	
	@Brick
	private Container _container;
	
	@Brick
	private BlinkingLights _exceptionHandler;

	private final transient Object _portToListenMonitor = new Object();
	private int _portToListen = 0;
	
	@Override
	public void start() throws Exception {
		//handle sneer port changes
		Threads.startDaemon(new Runnable(){ @Override public void run() {
			listenToSneerPort();
		}});
		_sneerPort.output().addReceiver(new Omnivore<Integer>() { @Override public void consume(Integer port) {
			setPort(port);
		}});
		
		//handle contact changes
		receiver = new SimpleListReceiver<Contact>(_contactManager.contacts()){

			@Override
			protected void elementAdded(Contact contact) {
				connect(contact.host(), contact.port());
			}

			@Override
			protected void elementPresent(Contact contact) {
				connect(contact.host(), contact.port());
			}
			
			@Override
			protected void elementToBeRemoved(Contact contact) {
				throw new NotImplementedYet();
			}};
			
			receiver.toString();
	}

	private void connect(String host, int port) {
		Connection conn = newConnection();
		conn.connect(host, port);
	}

	@Override
	public Connection newConnection() {
		return _container.create(Connection.class);
	}

	@Override
	synchronized public Consumer<Integer> sneerPortSetter() {
		return new IntegerConsumerBoundaries("Sneer Port", _sneerPort.setter(), 0, 65535);
	}

	@Override
	public Signal<Integer> sneerPort() {
		return _sneerPort.output();
	}

	private void setPort(int port) {
		synchronized (_portToListenMonitor) {
			_portToListen = port;
			_portToListenMonitor.notify();
		}
	}

	private void listenToSneerPort() {
		while (true) {
			int myPortToListen;
			synchronized (_portToListenMonitor) {
				myPortToListen = _portToListen;
			}
			
			crashServerSocketIfNecessary();
			openServerSocket(myPortToListen);	
			
			synchronized (_portToListenMonitor) {
				if (myPortToListen == _portToListen)
					Threads.waitWithoutInterruptions(_portToListenMonitor);
			}
		}
	}

	private void openServerSocket(int port) {
		if (port == 0) return;
		
		try {
			_log.info("starting server socket at {}", port);
			_serverSocket = _network.openServerSocket(port);
		} catch (IOException e) {
			_exceptionHandler.handle("Error trying to open socket at "+port, e);
		}
	}

	private void crashServerSocketIfNecessary() {
		if(_serverSocket == null) return;

		_log.info("crashing server socket");
		_serverSocket.crash();
	}
}