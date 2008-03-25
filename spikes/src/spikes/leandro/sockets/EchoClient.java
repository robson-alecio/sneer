package spikes.leandro.sockets;

import java.io.*;
import java.net.*;

public class EchoClient {
	public static void main(String[] args) throws Exception {

        Socket echoSocket = new Socket("localhost", 8080);
        PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String input;
        String fromServer;
        
        while ((input = read(stdIn)) != null) {
        	out.println(input);
        	fromServer = in.readLine();
        	System.out.println(fromServer);
        }

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
	}

	private static String read(BufferedReader reader) throws Exception {
		System.out.print("Enter some text: ");
		String result = reader.readLine();
		return result;
	}
}