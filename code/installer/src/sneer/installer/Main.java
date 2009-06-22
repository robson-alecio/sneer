package sneer.installer;

import sneer.main.Sneer;
import sneer.main.SneerDirectories;

public class Main {

	public static void main(String[] args) throws Exception {
		if(SneerDirectories.SNEER_HOME.exists()) new Installation();
		else new InstallationWizard();
		
		overcomeWebstartSecurityRestrictions();
		new Sneer();
	}

	private static void overcomeWebstartSecurityRestrictions() {
		System.setSecurityManager(new PermissiveSecurityManager());
	}

}