package sneer.network.impl;

import java.io.IOException;

import sneer.lego.Brick;
import sneer.lego.Startable;
import sneer.log.Logger;
import sneer.network.Network;
import sneer.network.NetworkException;
import sneer.network.Packet;
import sneer.network.SovereignPeer;
import wheel.io.network.ObjectServerSocket;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.io.network.OldNetworkImpl;

public class NetworkImpl implements Network, Startable {

    @Brick
    private Logger _log;
    
    private OldNetwork _oldNetwork;
    
    private ObjectServerSocket _serverSocket;

    private int _serverPort;
    
    @Override
    public void start() throws Exception
    {
        _oldNetwork = new OldNetworkImpl();
        _log.info("starting server socket {}", _serverPort);
        _serverSocket = _oldNetwork.openObjectServerSocket(_serverPort);
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
            _log.info("Opening socket to {}:{}",peer.address(), peer.port());
            return _oldNetwork.openSocket(peer.address(), peer.port());
        }
        catch (IOException e)
        {
            throw new NetworkException("Error creating socket "+peer.address()+":"+peer.port(), e);
        }
    }
}