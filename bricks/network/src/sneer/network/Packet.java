package sneer.network;

public interface Packet
{
    boolean success();

    Object payload();

    SovereignPeer peer();
}
