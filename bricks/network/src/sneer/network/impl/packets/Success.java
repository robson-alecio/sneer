package sneer.network.impl.packets;

import sneer.network.Packet;
import sneer.network.SovereignPeer;

public class Success
    implements Packet
{
    private static final Packet INSTANCE = new Success();
    
    private Success() {}
    
    public static final Packet instance() {
        return INSTANCE;
    }
    
    @Override
    public Object payload() {
        return null;
    }

    @Override
    public SovereignPeer peer() {
        return null;
    }

    @Override
    public boolean success() {
        return true;
    }

}
