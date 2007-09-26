package sneer.apps.asker.packet;

public class AskerResponse extends AskerPacket{

	public final boolean _accepted;

	public AskerResponse(long id, boolean accepted){
		_id = id;
		_accepted = accepted;
	}

	private static final long serialVersionUID = 1L;

}
