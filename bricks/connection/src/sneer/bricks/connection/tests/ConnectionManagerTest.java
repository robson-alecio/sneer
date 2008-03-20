package sneer.bricks.connection.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.After;
import org.junit.Test;

import sneer.bricks.connection.Connection;
import sneer.bricks.connection.ConnectionManager;
import sneer.lego.Brick;
import sneer.lego.tests.BrickTestSupport;

public class ConnectionManagerTest extends BrickTestSupport {

	@Brick
	private ConnectionManager _connectionManager;
	
	@After
	public void tearDown() throws Exception {
		_connectionManager = null;
		stopContainer();
	}
	
	@Test
	public void testConnect() throws Exception {
		EchoServer server = new EchoServer();
		server.start(9090);
		Connection conn = _connectionManager.newConnection();
		assertFalse(conn.isConnected().currentValue());
		conn.connect("localhost", 9090);
		conn.waitUntilConnected();
		assertTrue(conn.isConnected().currentValue());
	}
}

class EchoServer {
	ServerSocket _serverSocket;
	void start(int port) throws Exception {
		_serverSocket = new ServerSocket(port);
		System.out.println("Server started: "+port);
		Executor executor = Executors.newSingleThreadExecutor();
		executor.execute(new Runnable(){ @Override public void run() {
			try {
				Socket clientSocket = _serverSocket.accept();
				handleClient(clientSocket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}});
	}
	
	private void handleClient(Socket clientSocket) throws Exception {
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		InputStream is = clientSocket.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = in.readLine()) != null) {
			System.out.println("Server: "+line);
			out.print(line);
		}
	}
}
