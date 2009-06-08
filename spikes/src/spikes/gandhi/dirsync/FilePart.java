package spikes.gandhi.dirsync;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class FilePart implements Serializable{

	private byte[] _data;
    private FileInfo _info;
    private long _offset;
    
    public FilePart(FileInfo info,byte[] data,long offset) {
        this._info = info;
        this._data = data;
        this._offset = offset;
    }
    
    public void materialize(String localPath) throws FileNotFoundException,IOException{
        RandomAccessFile ram=new RandomAccessFile(localPath+getInfo().getPath(),"rw");
        ram.seek(_offset);
        ram.write(_data);
    }
    
    public static int limit=100000;
    
    public FileInfo getInfo() {
        return _info;
    }
    
    public long getOffset() {
        return _offset;
    }
    
    public byte[] getData() {
        return _data;
    }

    private static final long serialVersionUID = 1L;

}
