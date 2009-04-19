package spikes.sandro.swing.synth;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

public class RunMe {
	
	JFrame _frame = new JFrame("Toolbar Synth Test");

	public static void main(String args[]) throws Exception {
		new RunMe().initGui(10, 10, 300, 60);
	}

	void loadSynth() throws Exception {
		SynthLookAndFeel synth = new SynthLookAndFeel();
		InputStream is = RunMe.class.getResourceAsStream("toolbar.xml");
		synth.load(is, Toolbar.class);
		UIManager.setLookAndFeel(synth);
		is.close();
	}

	void initGui(int x, int y, int width, int height) {
		_frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Toolbar toolbar = new Toolbar("Olá Synth!");
		JPanel toolbarPanel = toolbar;
		_frame.setContentPane(toolbarPanel);

		_frame.setBounds(x, y, width, height);
		_frame.setVisible(true);
	}
}
