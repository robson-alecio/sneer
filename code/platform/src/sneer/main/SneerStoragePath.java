package sneer.main;

import java.io.File;

import sneer.foundation.brickness.SneerHome;

class SneerStoragePath implements SneerHome {

	@Override
	public String get() {
		return new File(userHome(), "sneer").getAbsolutePath();
	}
	
	private static String userHome() {
		String override = System.getProperty("sneer.home");
		if (override != null) return override;
		
		return System.getProperty("user.home");
	}


}
