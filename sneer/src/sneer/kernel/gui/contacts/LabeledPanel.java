package sneer.kernel.gui.contacts;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabeledPanel extends JPanel{


	public LabeledPanel(String title, JPanel panel, Dimension dimension){
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		if (dimension != null){
			setMinimumSize(dimension);
			setSize(dimension);
			setPreferredSize(dimension);
			setMaximumSize(dimension);
		}
		setAlignmentX(Component.CENTER_ALIGNMENT);
		add(new JLabel(title));
		add(panel);
	}
	
	private static final long serialVersionUID = 1L;
}
