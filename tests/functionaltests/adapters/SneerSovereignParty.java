package functionaltests.adapters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import sneer.contacts.ContactManager;
import sneer.lego.Brick;
import sneer.lego.ContainerUtils;
import spikes.legobricks.name.NameKeeper;
import functionaltests.SovereignParty;

public class SneerSovereignParty implements SovereignParty {

	@Brick
	private NameKeeper _keeper; //TODO: replance _name by _keeper to test persistence
	
	@Brick
	private ContactManager _contactManager;
	
	private List<Connection> _connections;
	
	private String _name;
	
	public SneerSovereignParty(String name) {
		ContainerUtils.getContainer().inject(this);
		_keeper.toString();
		_connections = new ArrayList<Connection>();
		setOwnName(name);
	}

	@Override
	public void connectTo(SovereignParty peer) {
	    if(_contactManager.add(peer)) {
	        System.out.println("peer "+peer.ownName()+" added");
	    }
	}

    public void _connectTo_OLD_(SovereignParty peer) {
	    Connection conn = getConnection(peer);
	    if(conn != null) return;
	    conn = new Connection(peer);
	    //System.out.println("new connection "+this.ownName()+" -> "+peer.ownName());
	    _connections.add(conn);
	    peer.connectTo(this);
	}

	
	@Override
	public void giveNicknameTo(SovereignParty peer, String nickname) {
	    Connection conn = getConnection(peer);
	    if(conn == null)
	        throw new IllegalArgumentException("Not connected to peer: "+peer.ownName());
	    
	    conn.nickName = nickname;
	}

	@Override
	public SovereignParty navigateTo(String... nicknamePath) {
	    SovereignParty result = null;
	    for (String nickname : nicknamePath)
        {
            Connection conn = getConnection(nickname);
            if(conn != null) {
                String[] path = (String[]) ArrayUtils.remove(nicknamePath, 0);
                if(path.length == 0) {
                    return conn.peer;
                }
                result = conn.peer.navigateTo(path);
            }
        }
	    return result;
	}

	@Override
	public String ownName() {
		return _name;
	}

	@Override
	public void setOwnName(String newName) {
		_name = newName;
	}

    private Connection getConnection(SovereignParty peer) {
        for (Connection conn : _connections)
        {
            if(conn.peer == peer)
                return conn;
        }
        return null;
    }

	private Connection getConnection(String nickName) {
	    for (Connection conn : _connections)
        {
            if(conn.nickName.equals(nickName))
                return conn;
        }
	    return null;
	}
	
    @Override
    public String toString() {
        return "peer: "+_name;
    }


	private class Connection {

	    SovereignParty peer;
	    String nickName;
	    
	    public Connection(SovereignParty other)
        {
	        peer = other;
	        nickName = other.ownName();
        }
	    
	    @Override
        public String toString() {
	        return "connection to peer: "+peer.ownName()+"["+nickName+"]";
	    }
	}


    @Override
    public String address()
    {
        return "127.0.0.1";
    }

    @Override
    public int port()
    {
        return 9090;
    }

}

