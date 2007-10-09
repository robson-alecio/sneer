package sneer.apps.publicfiles.packet;

import wheel.io.files.impl.FileInfo;

public class PleaseSendFile implements PublicFilesPacket{

	public final FileInfo _info;
	public final String _transferId;

	public PleaseSendFile(String transferId, FileInfo info){
		_transferId = transferId;
		_info = info;
	}

	public int type() {
		return PLEASE_SEND_FILE;
	}

	private static final long serialVersionUID = 1L;
}
