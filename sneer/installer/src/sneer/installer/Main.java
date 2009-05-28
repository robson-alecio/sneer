package sneer.installer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;

import main.SneerStoragePath;

public class Main {
	
	public static void main(String[] args) {
		try {
			SneerStoragePath sneerStoragePath = new SneerStoragePath();
			File sneerHome = new File(sneerStoragePath.get());
			
			if(sneerHome.exists() && sneerHome.isDirectory()){
				new SneerJockey(sneerStoragePath);
				return;
			}
			
			new Wizard(sneerStoragePath);
		} catch (Throwable throwable) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintWriter _log = new PrintWriter(out, true);
			throwable.printStackTrace(_log);
			
			Dialogs.show("Sneer Error:", throwable.getMessage() + "\n\n" + new String(out.toByteArray()),	"Exit");
			System.exit(1);
		}
	}
}