package sneer.apps.filetransfer;

public class FilePart {
	public String _filename;
	public long _filesize; //only needed because the progressbar needs a reference
	public byte[] _content;
	public long _offset;

	
	public FilePart(String filename, long filesize, byte[] content, long offset) {
		_filename = filename;
		_filesize = filesize;
		_content = content;
		_offset = offset;
	}
	
	@Override
	public String toString(){
		return _filename + " - " + _filesize + " - " + _content.length + " - " + _offset;
	}

}
