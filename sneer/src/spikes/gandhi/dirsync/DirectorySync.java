package spikes.gandhi.dirsync;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DirectorySync {
    private InputStream _in;
    private OutputStream _out;
    private String localPath;
    private String remotePath;
    private boolean receiverFinished;
    private boolean senderFinished;
    private List<FileInfo> localList;
    private List<FileInfo> remoteList;
    private List<FileInfo> diff;
    private boolean wait=true;
    
    public DirectorySync() {
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

                remotePath=(String)inObj.readObject();
                remoteList=readListOfFileInfo(inObj);
                
                diff=DirectoryUtil.diff(localList,remoteList);
                 
                wait=false;
                
                List<TransferableFile> transferables=readListOfTrasferableFile(inObj);
                for(TransferableFile item:transferables){
                    System.out.println("transferred: "+item.getReadPath()+" to "+item.getWritePath());
                }
                              
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            receiverFinished=true;
        }

		@SuppressWarnings("unchecked")
		private List<TransferableFile> readListOfTrasferableFile(	ObjectInputStream inObj) throws IOException, ClassNotFoundException {
			return (List<TransferableFile>)inObj.readObject();
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

                outObj.writeObject(localPath);
                outObj.writeObject(localList);
                outObj.flush();
                
                while(wait){
                    Thread.sleep(100);
                }
                
                List<TransferableFile> transferables=new ArrayList<TransferableFile>();
                for(FileInfo item:diff){
                    TransferableFile transfer=new TransferableFile(localPath+item.getPath(),remotePath+item.getPath());
                    transferables.add(transfer);
                }
                outObj.writeObject(transferables);
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            senderFinished=true;
        }
    }
    
    
    
}
