package functionaltests.adapters;

import sneer.contacts.ContactManager;
import sneer.lego.Brick;
import functionaltests.SovereignParty;

public class SneerSovereignParty implements SovereignParty {
	
	@Brick
	private ContactManager _contactManager;
	
	private String _name;
	
	private String _address;
	
	private int _port;
	
	public SneerSovereignParty(String address, int port, String name) {
		_address = address;
		_port = port;
		setOwnName(name);
	}

	@Override
	public void connectTo(SovereignParty peer) {
	    if(_contactManager.add(peer)) {
	        //System.out.println(_name+" <-> "+peer.ownName() +" "+_contactManager);
	    }
	}

	@Override
	public String ownName() {
		return _name;
	}

	@Override
	public void setOwnName(String newName) {
		_name = newName;
	}

    @Override
    public void giveNicknameTo(SovereignParty peer, String nickname)
    {
        _contactManager.alias(peer, nickname);
    }

    @Override
    public SovereignParty navigateTo(String... nicknamePath)
    {
        throw new wheel.lang.exceptions.NotImplementedYet();
    }

    @Override
    public String address()
    {
        return _address;
    }

    @Override
    public int port()
    {
        return _port;
    }

    @Override
    public String toString() {
        return "peer: "+_name;
    }

}

