package sneer.tests;

import sneer.InstallationWizard;
import sneer.kernel.SneerDirectories;

public class MainInstallationWizard {

	public static void main(String[] args) {
		System.out.println(SneerDirectories.sneerDirectory());
		new InstallationWizard(SneerDirectories.sneerDirectory());
	}

}
