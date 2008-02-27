package spikes.vitor.security;

import java.io.File;
import java.io.IOException;

public class BadFilePlugin {

	public static void run() {
		try {
			new File("text.txt").createNewFile();
			System.out.println("Creating file. This line could not be printed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
