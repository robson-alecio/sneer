package sneer.contacts.impl;

import java.util.ArrayList;
import java.util.List;

import functionaltests.SovereignParty;
import sneer.contacts.ContactManager;
import sneer.lego.Brick;
import sneer.network.Network;
import sneer.network.Packet;
import sneer.network.impl.packets.ConnectTo;

public class ContactManagerImpl implements ContactManager {

    @Brick
    private Network _network;
    
    private List<Contact> _contacts = new ArrayList<Contact>();
    
    @Override
    public boolean add(SovereignParty party) {
        Packet connectTo = new ConnectTo(party); //FixUrgent: create packet factory to hide packets implementation 
        Packet reply = _network.send(connectTo);
        boolean result = reply.success();
        if(result) _contacts.add(new Contact(party));
        return result;
    }

    @Override
    public void alias(SovereignParty party, String nickname) {
        for (Contact contact : _contacts) {
            if(contact.party == party) {
                contact.alias = nickname;
                return;
            }
        }
        throw new IllegalArgumentException("Can't find contact "+party.ownName());
    }
    
}

class Contact {
    
    SovereignParty party;
    
    String alias;

    public Contact(SovereignParty p) {
        party = p;
        alias = party.ownName();
    }

    @Override
    public String toString() {
        return alias+"("+party.address()+":"+party.port()+")";
    }
}