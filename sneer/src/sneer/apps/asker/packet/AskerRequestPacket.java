package sneer.apps.asker.packet;

public class AskerRequestPacket extends AskerPacket{

	public final String _message;
	public final AskerRequestPayload _payload;

	public AskerRequestPacket(long id, String message, AskerRequestPayload payload){
		_id = id;
		_message = message;
		_payload = payload;
	}
	
	private static final long serialVersionUID = 1L;

}
