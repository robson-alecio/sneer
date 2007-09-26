package sneer.apps.asker.packet;

public class AskerRequestPacket extends AskerPacket{

	public final AskerRequestPayload _payload;

	public AskerRequestPacket(long id, AskerRequestPayload payload){
		_id = id;
		_payload = payload;
	}
	
	private static final long serialVersionUID = 1L;

}
