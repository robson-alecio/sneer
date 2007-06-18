package sneer.tests;

import java.io.IOException;

import sneer.InstallationWizard;
import sneer.Sneer;
import sneer.SneerDirectories;

public class MainSkippingBoot {

	public static void main(String[] args) throws IOException {
		System.out.println(SneerDirectories.sneerDirectory());
		new InstallationWizard(SneerDirectories.sneerDirectory());
		new Sneer();
	}

}
