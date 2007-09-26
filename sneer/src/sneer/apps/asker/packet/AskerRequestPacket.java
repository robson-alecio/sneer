package sneer.apps.asker.packet;

public class AskerRequestPacket extends AskerPacket{

	public final String _message;

	public AskerRequestPacket(long id, String message){
		_id = id;
		_message = message;
	}
	
	private static final long serialVersionUID = 1L;

}
