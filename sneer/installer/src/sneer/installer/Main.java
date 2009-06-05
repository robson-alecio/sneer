package sneer.installer;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.SynthLookAndFeel;

public class Main {

	public static void main(String[] args) throws Exception {
		loadSynthLookAndFeel();
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

	private static void loadSynthLookAndFeel() throws UnsupportedLookAndFeelException, ParseException, IOException {
		SynthLookAndFeel _synth = new SynthLookAndFeel();
		UIManager.setLookAndFeel(_synth);
		_synth.load(Main.class.getResource("synth.xml"));
	}
}