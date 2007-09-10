package sneer.tests;

import java.io.IOException;

import sneer.InstallationWizard;
import sneer.Sneer;
import sneer.SneerDirectories;
import wheel.io.Log;

public class MainSkippingBoot {

	public static void main(String[] args) throws IOException {
		System.out.println(SneerDirectories.sneerDirectory());
		new InstallationWizard(SneerDirectories.sneerDirectory());
		try{
			new Sneer();
		}catch(Exception e){
			Log.log(e);
			e.printStackTrace();
		}
	}

}
