package sneer.bricks.connection.impl;

import java.io.IOException;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.bricks.exceptionhandler.ExceptionHandler;
import sneer.contacts.Contact;
import sneer.contacts.ContactManager;
import sneer.lego.Brick;
import sneer.lego.Container;
import sneer.lego.Startable;
import sneer.log.Logger;
import wheel.io.network.ObjectServerSocket;
import wheel.io.network.OldNetwork;
import wheel.lang.Consumer;
import wheel.lang.IntegerConsumerBoundaries;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.impl.SimpleListReceiver;
import functionaltests.adapters.SneerParty;

public class ConnectionManagerImpl implements ConnectionManager, Startable {

	private Object receiver;
	
	private Source<Integer> _sneerPort = new SourceImpl<Integer>(0);
	
	private OldNetwork _network;
	
	private ObjectServerSocket _serverSocket;
	
    @Brick
    private Logger _log;

	@Brick
	private ContactManager _contactManager;
	
	@Brick
	private Container _container;
	
	@Brick
	private ExceptionHandler _exceptionHandler;

	private final transient Object _portToListenMonitor = new Object();
	private int _portToListen = 0;
	
	@Override
	public void start() throws Exception {
		_network = SneerParty.network();
		
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
			
			closeServerSocketIfNecessary();
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
			_serverSocket = _network.openObjectServerSocket(port);
		} catch (IOException e) {
			_exceptionHandler.handle("Error trying to open socket at "+port, e);
		}
	}

	private void closeServerSocketIfNecessary() {
		if(_serverSocket == null) return;

		_log.info("closing server socket");
		_serverSocket.close();
	}
}