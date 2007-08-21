package sneer.apps.metoo.packet;

import sneer.apps.metoo.MeTooPacket;

public class AppFilePart implements MeTooPacket{
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
