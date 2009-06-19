package sneer.installer;

import sneer.main.Sneer;

public class Main {

	public static void main(String[] args) throws Exception {
		new InstallationWizard();
		overcomeWebstartSecurityRestrictions();
		new Sneer();
	}


	private static void overcomeWebstartSecurityRestrictions() {
		System.setSecurityManager(new PermissiveSecurityManager());
	}

}