/**
 * 
 */
package spikes.sandro.swing.synth;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Toolbar extends JPanel{
	
	private final JLabel _title = new JLabel();
	private final JButton _menu = new JButton();
	
	Toolbar(String title){
		initGui(title);
		addSynthLoadAction();
		
		_menu.setName("InstrumentMenuButton");
	}
	
	private void initGui(String title) {
		_title.setOpaque(false);
		if (title != null)
			_title.setText(title);
		
		setLayout(new BorderLayout());
		add(_title, BorderLayout.CENTER);
		add(_menu, BorderLayout.EAST);
	}

	private void addSynthLoadAction() {
		_menu.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			try {
				RunMe test = new RunMe();		
				test.loadSynth();
				test.initGui(10, 110, 300, 60);
			} catch (Exception e1) {
				throw new sneer.commons.lang.exceptions.NotImplementedYet(e1); // Fix Handle this exception.
			}
		}});
	}
}