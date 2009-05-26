package sneer.installer;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

import main.SneerStoragePath;
import sneer.brickness.Brickness;
import sneer.brickness.BricknessFactory;

public class Main {
	
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(new NimbusLookAndFeel());
		SneerStoragePath sneerStoragePath = new SneerStoragePath();
		File sneerHome = new File(sneerStoragePath.get());
		
		if(sneerHome.exists() && sneerHome.isDirectory()){
			Brickness container = BricknessFactory.newBrickContainer(sneerStoragePath);
			File classBricksDirectory = new File(sneerHome, "bin");
			container.placeBrick(classBricksDirectory, sneer.pulp.threads.Threads.class.getName());
			return;
		}
		
		new Wizard(sneerHome);
	}
}
