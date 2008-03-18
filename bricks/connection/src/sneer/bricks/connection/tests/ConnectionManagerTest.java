package sneer.bricks.connection.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
	public void testIsOnline() throws Exception {
		Connection conn = _connectionManager.openConnection("localhost", 9090);
		assertTrue(conn.isOnline().currentValue());
	}

	@Test
	public void testConnectTwice() throws Exception {
		Connection conn1 = _connectionManager.openConnection("localhost", 9090);
		Connection conn2 = _connectionManager.openConnection("localhost", 9090);
		assertSame(conn1, conn2);
	}

	@Test
	public void testCloseConnection() throws Exception {
		Connection conn = _connectionManager.openConnection("localhost", 9091);
		assertTrue(conn.isOnline().currentValue());
		conn.close();
		assertFalse(conn.isOnline().currentValue());
	}
	
	@Test
	public void testListConnections() throws Exception {
		List<Connection> connections = _connectionManager.listConnections();
		assertEquals(0, connections.size());
		
		Connection conn = _connectionManager.openConnection("localhost", 9090);
		connections = _connectionManager.listConnections();
		assertEquals(1, connections.size());
		assertSame(conn, connections.get(0));

		//same connection
		conn = _connectionManager.openConnection("localhost", 9090);
		connections = _connectionManager.listConnections();
		assertEquals(1, connections.size());
		assertSame(conn, connections.get(0));

		//other
		conn = _connectionManager.openConnection("localhost", 9091);
		connections = _connectionManager.listConnections();
		assertEquals(2, connections.size());
	}
}
