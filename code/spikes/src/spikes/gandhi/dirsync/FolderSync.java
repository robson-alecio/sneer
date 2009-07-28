package spikes.gandhi.dirsync;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

public class FolderSync {
    private InputStream _in;
    private OutputStream _out;
    private String localPath;
    private boolean receiverFinished;
    private boolean senderFinished;
    private List<FileInfo> localList;
    private List<FileInfo> remoteList;
    private List<FileInfo> diff;
    private boolean wait=true;
    
    public FolderSync() {
    }
    
    public void sync(String path, InputStream in, OutputStream out) {
        this.localPath=path;
        this._in = in;
        this._out = out;
        localList=DirectoryUtil.list(path);
        
        Receiver receiver=new Receiver();
        receiver.setDaemon(true);
        receiver.start();
        Sender sender=new Sender();
        sender.setDaemon(true);
        sender.start();
    }
    
    public boolean isFinished(){
        return (receiverFinished&&senderFinished);
    }
    
    private class Receiver extends Thread{
        @Override
        public void run(){
            try{
                ObjectInputStream inObj=new ObjectInputStream(_in);
                
                remoteList=readListOfFileInfo(inObj);
                
                diff=DirectoryUtil.diff(localList,remoteList);
                
                wait=false;
                
                readFileInfos(inObj);
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            receiverFinished=true;
        }
        
        private void readFileInfos( ObjectInputStream inObj) throws IOException, ClassNotFoundException {
            int size=inObj.readInt();
            for(int t=0;t<size;t++){
                FileInfo info = (FileInfo)inObj.readObject();
                File localFile = new File(localPath+info.getPath());
                localFile.delete();
                System.out.println("materializando arquivo "+info.getPath());
                materializeFileParts(inObj);
                localFile.setLastModified(info.getLastModified());
            }
        }
        
        private void materializeFileParts( ObjectInputStream inObj) throws IOException, ClassNotFoundException {
            long count=inObj.readLong();
            for(long y=0;y<count;y++){
                FilePart part = (FilePart)inObj.readObject();
                System.out.println("materializando bytes "+part.getOffset()+" a "+(part.getOffset()+part.getData().length)+" do arquivo "+part.getInfo().getPath());
                part.materialize(localPath);
            }
        }
        
        
        @SuppressWarnings("unchecked")
        private List<FileInfo> readListOfFileInfo(ObjectInputStream inObj) throws IOException,
                ClassNotFoundException {
            return (List)inObj.readObject();
        }
    }
    
    private class Sender extends Thread{
        @Override
        public void run(){
            try{
                ObjectOutputStream outObj=new ObjectOutputStream(_out);
                
                outObj.writeObject(localList);
                outObj.flush();
                
                while(wait){
                    Thread.sleep(100);
                }
                
                writeFileInfos(outObj);
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            senderFinished=true;
        }
        
        private void writeFileInfos(ObjectOutputStream outObj) throws IOException{
            outObj.writeInt(diff.size());
            outObj.flush();
            for(FileInfo item:diff){
                outObj.writeObject(item);
                writeFileParts(item, outObj);
            }
        }
        
        private void writeFileParts(FileInfo item, ObjectOutputStream outObj) throws IOException{
        	
            FilePartIterator iterator=new FilePartIterator(item,localPath+item.getPath());
            outObj.writeLong(iterator.count());
            outObj.flush();
            while(iterator.hasNext()){
                outObj.writeObject(iterator.next());
            }
        }
        
    }
    
    
    
}
