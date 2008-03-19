package sneer.bricks.connection.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.contacts.Contact;
import sneer.contacts.ContactManager;
import sneer.lego.Brick;
import sneer.lego.Startable;
import sneer.log.Logger;
import wheel.io.network.ObjectServerSocket;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.lang.Consumer;
import wheel.lang.IntegerConsumerBoundaries;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.impl.SimpleListReceiver;
import functionaltests.adapters.SneerParty;

public class ConnectionManagerImpl implements ConnectionManager, Startable {

	private Map<String, Connection> _map = new HashMap<String, Connection>();

	private Object receiver;
	
	private Source<Integer> _sneerPort = new SourceImpl<Integer>(0);
	
	private OldNetwork _network;
	
	private ObjectServerSocket _serverSocket;
	
    @Brick
    private Logger _log;

	@Brick
	private ContactManager _contactManager;
	
	@Override
	public void start() throws Exception {
		_network = SneerParty.network();
		receiver = new SimpleListReceiver<Contact>(_contactManager.contacts()){
			
			//FixUrgent All events have to be processed asynchronously.
			
			@Override
			protected void elementAdded(Contact contact) {
				openConnection(contact.host(), contact.port());
			}
			
			@Override
			protected void elementPresent(Contact contact) {
				openConnection(contact.host(), contact.port());
			}
			
			@Override
			protected void elementToBeRemoved(Contact contact) {
				Connection conn = getConnection(connectionId(contact.host(), contact.port()));
				conn.close();
			}};
			
			receiver.toString();
	}

	private String connectionId(String host, int port) {
		return host+":"+port;
	}
	
	protected Connection getConnection(String connectionId) {
		for(String key : _map.keySet()) {
			if(key.equals(connectionId)) {
				return _map.get(key);
			}
		}
		throw new IllegalArgumentException("Can't find connection: "+connectionId);
	}

	@Override
	public List<Connection> listConnections() {
		List<Connection> result = new ArrayList<Connection>(_map.values());
		return result;
	}

	@Override
	public Connection openConnection(String host, int port) {
		String connectionId = connectionId(host, port);
		Connection conn = _map.get(connectionId);
		if(conn != null) return conn;
		_log.info("Opening connection to {}:{}",host, port);
		ObjectSocket socket = null;
		try {
			socket = _network.openSocket(host, port);
		} catch (IOException e) {
			e.printStackTrace(); //FixUrgent: handle exception
		}
		conn = new ConnectionImpl(socket);
		_map.put(connectionId, conn);
		return conn;
	}

	@Override
	synchronized public Consumer<Integer> sneerPortSetter() {
		return new IntegerConsumerBoundaries("Sneer Port", _sneerPort.setter(), 0, 65535);
	}

	private void setPort() {
//		if(_serverSocket != null) {
//			_log.info("closing server socket at {}", _sneerPort);
//			_serverSocket.close();
//		}
//		_sneerPort = port;
//		try {
//			_log.info("starting server socket at {}", _sneerPort);
//			_serverSocket = _network.openObjectServerSocket(port);
//		} catch (IOException e) {
//			e.printStackTrace(); //FixUrgent: handle exception
//		}
	}

	@Override
	public Signal<Integer> sneerPort() {
		return _sneerPort.output();
	}
	
	
}