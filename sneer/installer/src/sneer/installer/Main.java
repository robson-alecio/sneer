package sneer.installer;

import java.io.File;

public class Main {

	public static void main(String[] args) throws Exception {
		new Wizard(sneerHome());

		new SneerJockey(sneerHome());
	}
	
	static private File sneerHome() {
		return new File(userHome(), ".sneer");
	}
	
	static private String userHome() {
		String override = System.getProperty("home_override");
		if (override != null) return override;
		
		return System.getProperty("user.home");
	}

}