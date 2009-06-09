package sneer.main;

import java.io.File;

import sneer.foundation.brickness.StoragePath;

public class SneerStoragePath implements StoragePath {

	@Override
	public String get() {
		return new File(userHome(), ".sneer").getAbsolutePath();
	}
	
	private static String userHome() {
		String override = System.getProperty("home_override");
		if (override != null) return override;
		
		return System.getProperty("user.home");
	}


}
