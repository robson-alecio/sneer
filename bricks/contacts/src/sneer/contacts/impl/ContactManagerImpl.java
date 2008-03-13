package sneer.contacts.impl;

import functionaltests.SovereignParty;
import sneer.contacts.ContactManager;
import sneer.lego.Brick;
import sneer.network.Network;
import sneer.network.Packet;
import sneer.network.impl.packets.ConnectTo;

public class ContactManagerImpl implements ContactManager {

    @Brick
    private Network _network;
    
    @Override
    public boolean add(SovereignParty party)
    {
        Packet connectTo = new ConnectTo(party); //FixUrgent: create packet factory to hide packets implementation 
        Packet reply = _network.send(connectTo);
        return reply.success();
    }
    
}