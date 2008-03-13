package sneer.network.impl.tests;

import java.util.List;

import sneer.network.Network;
import sneer.network.Packet;
import sneer.network.SovereignPeer;
import sneer.network.impl.packets.Success;

public class MockNetwork
    implements Network
{
    List<SovereignPeer> _peers;
    
    @Override
    public Packet send(Packet packet)
    {
        SovereignPeer peer = packet.peer();
        System.out.println("Sending message to "+peer.address()+":"+peer.port());
        //FixUrgent: find contact manager from sender and add the sender to peer contact list
        return Success.instance();
    }

}
