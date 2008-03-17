package sneer.contacts.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.contacts.ConnectionManager;
import sneer.contacts.ContactId;
import sneer.lego.Brick;
import sneer.network.Network;
import sneer.network.SovereignPeer;
import wheel.lang.exceptions.NotImplementedYet;

public class ConnectionManagerImpl implements ConnectionManager {

    @Brick
    private Network _network;
    
    private List<Contact> _contacts = new ArrayList<Contact>();
    
    @Override
    public boolean add(String nickame, String hostAddress, int sneerPort) {
//        if(result) _contacts.add(new Contact(party));
//        return result;
    	throw new NotImplementedYet();
    }

	@Override
	public ContactId currentContact(String nick) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

    
}

class Contact {
    
    SovereignPeer party;
    
    String alias;

    public Contact(SovereignPeer p) {
        party = p;
        alias = party.ownName();
    }

    @Override
    public String toString() {
        return alias+"("+party.address()+":"+party.port()+")";
    }
}