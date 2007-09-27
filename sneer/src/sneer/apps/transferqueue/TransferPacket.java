package sneer.apps.transferqueue;

import java.io.Serializable;

public class TransferPacket implements Serializable{

	public final String _transferId;
	public final byte[] _contents;
	public final long _offset;

	public TransferPacket(String transferId, byte[] contents, long offset){
		_transferId = transferId;
		_contents = contents;
		_offset = offset;
	}

	private static final long serialVersionUID = 1L;

}
