package sneer.contacts.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.contacts.ContactManager;
import sneer.lego.Brick;
import sneer.network.Network;
import sneer.network.SovereignPeer;
import wheel.lang.exceptions.NotImplementedYet;

public class ContactManagerImpl implements ContactManager {

    @Brick
    private Network _network;
    
    private List<Contact> _contacts = new ArrayList<Contact>();
    
    @Override
    public boolean add(String nickame, String hostAddress, int sneerPort) {
//        if(result) _contacts.add(new Contact(party));
//        return result;
    	throw new NotImplementedYet();
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