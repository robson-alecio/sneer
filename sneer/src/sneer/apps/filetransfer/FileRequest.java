package sneer.apps.filetransfer;

import sneer.apps.asker.packet.AskerRequestPayload;

public class FileRequest implements AskerRequestPayload{

	public final String _filename;
	public final long _size;

	public FileRequest(String filename, long size){
		_filename = filename;
		_size = size;
	}
	
	private static final long serialVersionUID = 1L;

}
