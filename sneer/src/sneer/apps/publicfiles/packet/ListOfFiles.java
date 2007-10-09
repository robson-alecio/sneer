package sneer.apps.publicfiles.packet;

import wheel.io.files.impl.FileInfo;

public class ListOfFiles implements PublicFilesPacket{

	public final FileInfo[] _infos;

	public ListOfFiles(FileInfo[] infos){
		_infos = infos;
	}

	public int type() {
		return LIST_OF_FILES;
	}

	private static final long serialVersionUID = 1L;
}
