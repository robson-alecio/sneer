package sneer.kernel.appmanager.metoo.packet;

import sneer.kernel.appmanager.metoo.MeTooPacket;

public class AppFilePart implements MeTooPacket {

	private static final long serialVersionUID = 1L;

	public String _filename;
	public long _filesize; 
	public byte[] _content;
	public long _offset;

	
	public AppFilePart(String filename, long filesize, byte[] content, long offset) {
		_filename = filename;
		_filesize = filesize;
		_content = content;
		_offset = offset;
	}
	
	public int type() {
		return APP_FILE_PART; 
	}

}
