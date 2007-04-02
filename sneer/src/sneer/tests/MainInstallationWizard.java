package sneer.tests;

import java.io.IOException;

import sneer.InstallationWizard;
import sneer.SneerDirectories;

public class MainInstallationWizard {

	public static void main(String[] args) throws IOException {
		System.out.println(SneerDirectories.sneerDirectory());
		new InstallationWizard(SneerDirectories.sneerDirectory());
	}

}
