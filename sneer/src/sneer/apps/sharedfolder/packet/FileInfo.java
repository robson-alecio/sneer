package sneer.apps.sharedfolder.packet;

import java.io.Serializable;

public class FileInfo implements Serializable{

	public final String _name;
	public final long _size;

	public FileInfo(String name, long size){
		_name = name.toLowerCase().replace('\\', '/');
		_size = size;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof FileInfo)) return false;
		return _name.equals(((FileInfo)obj)._name);
	}

	@Override
	public int hashCode() {
		return 0;
	}

	private static final long serialVersionUID = 1L;
}
