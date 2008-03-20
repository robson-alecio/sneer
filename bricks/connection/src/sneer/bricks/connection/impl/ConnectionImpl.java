package sneer.bricks.connection.impl;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import sneer.bricks.connection.Connection;
import sneer.bricks.exceptionhandler.ExceptionHandler;
import sneer.lego.Brick;
import sneer.log.Logger;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.io.network.OldNetworkImpl;
import wheel.lang.Threads;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;

public class ConnectionImpl implements Connection {

	private Source<Boolean> _status = new SourceImpl<Boolean>(false);

	private OldNetwork _network = new OldNetworkImpl(); //TODO: inject

	private String _host;
	
	private int _port;
	
	private ExecutorService executor = Executors.newCachedThreadPool(); //TODO: inject
	
	private FutureTask<ObjectSocket> task;
	
    @Brick
    private Logger _log; //TODO: inject
    
    @Brick
    private ExceptionHandler _exceptionHandler; //TODO: inject

	@Override
	public void close() {
		throw new NotImplementedYet();
	}
	
	public void connect(final String host, final int port) {

		if(task != null && !task.isDone()) {
			if(!_host.equalsIgnoreCase(host) || _port != port) {
				//cancel ongoing connection and connect to a new host
				task.cancel(true);
				task = null;
			}

		}
		
		_host = host;
		_port = port;
		
		if(task == null) {
			task = new FutureTask<ObjectSocket>(new Callable<ObjectSocket>() { 
				
				private ObjectSocket _socket;

				@Override
				public ObjectSocket call() throws Exception {
					_log.info("Opening connection to {}:{}",host, port);
					try {
						_socket = _network.openSocket(host, port);
						_socket.writeObject("connection string\n");
						String reply = (String) _socket.readObject();
						System.out.println("Reply: "+reply);
						_status.setter().consume(Boolean.TRUE);
						synchronized (this) {
							notify();
						}
					} catch (IOException e) {
						_exceptionHandler.handle("error openning connection to "+_host+":"+_port, e);
					}
					return _socket;
				}
			});
		}
		if(isConnected().currentValue()) return;
		executor.execute(task);
	}

	public Signal<Boolean> isConnected() {
		return _status.output();
	}

	@Override
	public void waitUntilConnected() {
		synchronized (this) {
			while(!isConnected().currentValue()) Threads.waitWithoutInterruptions(this);
		}
	}

	@Override
	public Object read() throws Exception {
		ObjectSocket socket = task.get();
		return socket.readObject();
	}

	@Override
	public void write(Object packet) throws Exception {
		ObjectSocket socket = task.get();
		socket.writeObject(packet);
	}
}