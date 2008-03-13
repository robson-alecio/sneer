package sneer.network.impl;

import java.io.IOException;

import sneer.lego.Startable;
import sneer.network.Network;
import sneer.network.NetworkException;
import sneer.network.Packet;
import sneer.network.SovereignPeer;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.io.network.OldNetworkImpl;

public class NetworkImpl implements Network, Startable {

    private OldNetwork _oldNetwork;
    
    @Override
    public void start()
    {
        _oldNetwork = new OldNetworkImpl();
        //TODO: start listening on a local port so we can talk to the world
    }
    
    @Override
    public Packet send(Packet packet)
    {
        Packet reply;
        SovereignPeer peer = packet.peer();
        ObjectSocket socket = openSocket(peer);
        try {
            socket.writeObject(packet);
            reply = (Packet) socket.readObject();
        } catch(Exception e) {
            throw new NetworkException("Error sending packet", e);
        }
        return reply;
    }

    private ObjectSocket openSocket(SovereignPeer peer)
    {
        try
        {
            return _oldNetwork.openSocket(peer.address(), peer.port());
        }
        catch (IOException e)
        {
            throw new NetworkException("Error creating socket "+peer.address()+":"+peer.port(), e);
        }
    }
}