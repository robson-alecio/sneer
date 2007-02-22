package spikes.gandhi.dirsync;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TransferableFile implements Externalizable{

	private String _readPath;
    private String _writePath;
    
    public TransferableFile(){
    }
    
    public TransferableFile(String readPath,String writePath) {
        this._readPath=readPath;
        this._writePath=writePath;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        _readPath=(String)in.readObject();
        _writePath=(String)in.readObject();
        long length=in.readLong();
        long lastModified=in.readLong();
        File file=new File(getWritePath());
        file.delete();
        FileOutputStream out=new FileOutputStream(file);
        for(int t=0;t<length;t++){
            out.write(in.readByte());
            out.flush();
        }
        file.setLastModified(lastModified);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        File file=new File(_readPath);
        long length=file.length();
        long lastModified=file.lastModified();
        out.writeObject(_readPath);
        out.writeObject(_writePath);
        out.writeLong(length);
        out.writeLong(lastModified);
        FileInputStream in=new FileInputStream(file);
        int c;
        while((c=in.read())!=-1){
            out.write(c);
        }
        out.flush();
    }

    public String getReadPath() {
        return _readPath;
    }

    public String getWritePath() {
        return _writePath;
    }

    private static final long serialVersionUID = 1L;

}
