package spikes.gandhi.dirsync;

import java.io.Serializable;

public class FileInfo implements Serializable{
	private String _path;
    private long _size;
    private long _lastModified;
    
    public FileInfo(String path, long size, long lastModified) {
        this._path=path;
        this._size=size;
        this._lastModified=lastModified;
    }
    
    @Override
	public boolean equals(Object object){
        if (!(object instanceof FileInfo))
            return false;
        FileInfo temp=(FileInfo)object;
        return ((temp.getPath().equals(getPath()))&&(temp.getSize()==getSize())&&(temp.getLastModified()==getLastModified()));
    }

    public String getPath() {
        return _path;
    }

    public long getSize() {
        return _size;
    }

    public long getLastModified() {
        return _lastModified;
    }

	private static final long serialVersionUID = 1L;

}
