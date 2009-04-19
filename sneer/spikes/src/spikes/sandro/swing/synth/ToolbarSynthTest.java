package spikes.sandro.swing.synth;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

public class ToolbarSynthTest {
	
	JFrame _frame = new JFrame("Toolbar Synth Test");

	public static void main(String args[]) throws Exception {
		new ToolbarSynthTest().initGui(10, 10, 300, 60);
	}

	private void loadSynth() throws Exception {
		SynthLookAndFeel synth = new SynthLookAndFeel();
		InputStream is = ToolbarSynthTest.class.getResourceAsStream("toolbar.xml");
		synth.load(is, ToolbarSynthTest.class);
		UIManager.setLookAndFeel(synth);
		is.close();
	}

	private void initGui(int x, int y, int width, int height) {
		_frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Toolbar toolbar = new Toolbar("Olá Synth!");
		JPanel toolbarPanel = toolbar._toolbarPanel;
		_frame.setContentPane(toolbarPanel);

		_frame.setBounds(x, y, width, height);
		_frame.setVisible(true);
	}
	
	private class Toolbar{
		
		private final JPanel _toolbarPanel = new JPanel();
		private final JLabel _title = new JLabel();
		private final JButton _menu = new JButton();
		
		private Toolbar(String title){
			initGui(title);
			addSynthLoadAction();
		}
		
		private void initGui(String title) {
			_title.setOpaque(false);
			if (title != null)
				_title.setText(title);
			
			_toolbarPanel.setLayout(new BorderLayout());
			_toolbarPanel.add(_title, BorderLayout.CENTER);
			_toolbarPanel.add(_menu, BorderLayout.EAST);
		}

		private void addSynthLoadAction() {
			_menu.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
				try {
					ToolbarSynthTest test = new ToolbarSynthTest();		
					test.loadSynth();
					test.initGui(10, _frame.getLocationOnScreen().x + 100, 300, 60);
				} catch (Exception e1) {
					throw new sneer.commons.lang.exceptions.NotImplementedYet(e1); // Fix Handle this exception.
				}
			}});
		}
	}
}
