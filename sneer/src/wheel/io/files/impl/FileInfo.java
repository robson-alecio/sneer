package wheel.io.files.impl;

import java.io.Serializable;

public class FileInfo implements Serializable, Comparable<FileInfo>{

	public final String _name;
	public final long _size;

	public FileInfo(String name, long size){
		_name = WindowsAndLinuxCompatibility.normalizePath(name);
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

	public int compareTo(FileInfo info) {
		return _name.compareTo(info._name);
	}
	
	@Override
	public String toString(){
		return _name + " - " + _size + "bytes";
	}
	
	private static final long serialVersionUID = 1L;
}
