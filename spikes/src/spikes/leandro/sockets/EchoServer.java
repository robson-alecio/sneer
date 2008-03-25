package spikes.leandro.sockets;

import java.net.*;
import java.io.*;

public class EchoServer {
    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Echo server started");
        Socket clientSocket = serverSocket.accept();
        System.out.println("connection accepted");

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));
        String line;
        
        while ((line = in.readLine()) != null) {
        	System.out.println("from client: "+line);
        	out.println(">> "+line);
        	if (line.equals("quit"))
        		break;
        }
        
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}