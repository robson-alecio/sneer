package spikes.gandhi.dirsync;

import java.net.InetAddress;
import java.net.Socket;

public class SideB {
    
    public SideB() {
        
        try {
            InetAddress addr = InetAddress.getLocalHost();
            int port = 2000;
            Socket socket = new Socket(addr, port);
            
            FolderSync sync=new FolderSync();
            sync.sync("/tmp/sideb",socket.getInputStream(),socket.getOutputStream());
            
            while(!sync.isFinished()){
                Thread.sleep(3000);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        new SideB();
    }
    
}
