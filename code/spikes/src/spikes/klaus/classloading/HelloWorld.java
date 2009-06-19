package spikes.klaus.classloading;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;


public class HelloWorld {

	static FileOutputStream fos = fos();
	static ServerSocket ss = ss();
	
	public HelloWorld() {
		System.out.println("Hello World");
	}

	private static ServerSocket ss() {
		try {
			return new ServerSocket(12345);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private static FileOutputStream fos() {
		try {
			return new FileOutputStream("tmp.tmp");
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

}
