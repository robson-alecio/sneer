package spikes.gandhi.dirsync;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SideA {
    
    public SideA() {
        try {
            int port = 2000;
            ServerSocket srv = new ServerSocket(port);
            Socket socket = srv.accept();
            
            DirectorySync sync=new DirectorySync();
            sync.sync("/tmp/sidea",socket.getInputStream(),socket.getOutputStream());
            
            while(!sync.isFinished()){
                Thread.sleep(100);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        new SideA();
    }
    
}
