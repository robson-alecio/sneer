package functionaltests.adapters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import sneer.lego.Brick;
import sneer.lego.ContainerUtils;
import spikes.legobricks.name.NameKeeper;
import functionaltests.SovereignParty;

public class SneerSovereignParty implements SovereignParty {

	@Brick
	private NameKeeper _keeper;
	
	private List<Connection> _connections;
	
	public SneerSovereignParty(String name) {
		ContainerUtils.getContainer().inject(this);
		_connections = new ArrayList<Connection>();
		setOwnName(name);
	}

	@Override
	public void connectTo(SovereignParty peer) {
	    Connection conn = getByNickName(peer.ownName());
	    if(conn != null) return;
	    conn = new Connection(peer);
	    System.out.println("new connection "+this.ownName()+" -> "+peer.ownName());
	    _connections.add(conn);
	}

	@Override
	public void giveNicknameTo(SovereignParty peer, String nickname) {
	    Connection conn = getByNickName(peer.ownName());
	    if(conn != null)
	        conn.nickName = nickname;
	}

	@Override
	public SovereignParty navigateTo(String... nicknamePath) {
	    SovereignParty result = null;
	    for (String nickname : nicknamePath)
        {
            Connection conn = getByNickName(nickname);
            if(conn != null) {
                String[] path = (String[]) ArrayUtils.remove(nicknamePath, 0);
                result = conn.party.navigateTo(path);
            }
        }
	    return result;
	}

	@Override
	public String ownName() {
		return _keeper.getName();
	}

	@Override
	public void setOwnName(String newName) {
		_keeper.setName(newName);
	}

	private Connection getByNickName(String nickName) {
	    for (Connection conn : _connections)
        {
            if(conn.nickName.equals(nickName))
                return conn;
        }
	    return null;
	}

	private class Connection {

	    SovereignParty party;
	    String nickName;
	    
	    public Connection(SovereignParty peer)
        {
	        party = peer;
	        nickName = peer.ownName();
        }
	}

}

