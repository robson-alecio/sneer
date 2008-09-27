package spikes.gandhi.dirsync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FilePartIterator {
    
    public static long _limit = 100000;
    private long _offset = 0;
    private File _file;
    private FileInfo _info;
    
    
    public FilePartIterator(FileInfo info,String readPath) {
        _info=info;
        _file = new File(readPath);
    }
    
    public long count(){
        long count = _file.length()/_limit;
        if ((_file.length()%_limit)!=0)
            count+=1;
        return count;
    }
    
    public boolean hasNext(){
        return (_offset!=_file.length());
    }
    
    public FilePart next() throws FileNotFoundException,IOException{
        RandomAccessFile ram=new RandomAccessFile(_file,"r");
        ram.seek(_offset);
        long start = _offset;
        long limit = _limit;
        if (_offset > (_file.length() - limit))
            limit=_file.length() - _offset;
        byte[] data=new byte[(int)limit];
        int readed=ram.read(data);
        int total=readed;
        while((readed!=-1)&&(total!=data.length)){
            readed=ram.read(data,total,data.length-total);
            total+=readed;
        }
        _offset = _offset+limit;
        return new FilePart(_info,data,start);
    }
    
    public void setOffset(long offset){
        this._offset=offset;
    }
    
}
