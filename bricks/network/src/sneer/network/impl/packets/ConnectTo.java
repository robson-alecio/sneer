package sneer.network.impl.packets;

import sneer.network.Packet;
import sneer.network.SovereignPeer;

public class ConnectTo implements Packet {

    private SovereignPeer _peer;
    
    public ConnectTo(SovereignPeer peer)
    {
        _peer = peer;
    }

    @Override
    public Object payload()
    {
        throw new wheel.lang.exceptions.NotImplementedYet();
    }

    @Override
    public boolean success()
    {
        throw new wheel.lang.exceptions.NotImplementedYet();
    }

    @Override
    public SovereignPeer peer()
    {
        return _peer;
    }

}
