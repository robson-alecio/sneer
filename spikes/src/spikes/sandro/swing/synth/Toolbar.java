package spikes.sandro.swing.synth;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

import java.awt.BorderLayout;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

public class Toolbar extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JFrame _frame = new JFrame("Toolbar Synth Test");
	private final JLabel _title = new JLabel();
	private final JButton _menu = new JButton();
	
	public Toolbar(String title){
		initGui(title);
		initSynthNames();
	}
	
	private void initSynthNames() {
		_menu.setName("InstrumentMenuButton");
		_title.setName("InstrumentTitle");
		setName("InstrumentToolbar");
	}
	
	private void initGui(String title) {
		_title.setOpaque(false);
		if (title != null)
			_title.setText(title);
		
		setLayout(new BorderLayout());
		add(_title, BorderLayout.CENTER);
		add(_menu, BorderLayout.EAST);
	}

	private static void loadSynth() throws Exception {
		SynthLookAndFeel synth = new SynthLookAndFeel();
		loadFile(synth, "toolbar.xml");
		loadFile(synth, "buttons.xml");
		UIManager.setLookAndFeel(synth);
	}

	private static void loadFile(SynthLookAndFeel synth, String fileName) {
		InputStream is = null;
		try {
			is = Toolbar.class.getResourceAsStream(fileName);
			synth.load(is, Toolbar.class);
		} catch (Exception e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} finally {
			try { is.close(); } catch (Exception e2) { /* ignore */ }
		}
	}

	private void initGui(int x, int y, int width, int height) {
		_frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		_frame.setContentPane(this);
		_frame.setBounds(x, y, width, height);
		_frame.setVisible(true);
	}
	
	public static void main(String args[]) throws Exception {
		Toolbar.loadSynth();
		Toolbar toolbar = new Toolbar("Olá Synth!");
		toolbar.initGui(10, 10, 300, 60);
	}
}