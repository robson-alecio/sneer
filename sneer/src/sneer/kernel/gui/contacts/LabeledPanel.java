package sneer.kernel.gui.contacts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class LabeledPanel extends JPanel{

	public LabeledPanel(String title, JPanel panel, Dimension dimension){
		setBorder(new EmptyBorder(3,3,3,3));
		setLayout(new BorderLayout());
		if (dimension != null){
			setMinimumSize(dimension);
			setSize(dimension);
			setPreferredSize(dimension);
			setMaximumSize(dimension);
		}
		JLabel label = new JLabel(title);
		label.setFont(FontUtil.getFont(14));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		add(label,BorderLayout.NORTH);
		add(panel,BorderLayout.CENTER);
	}
	
	private static final long serialVersionUID = 1L;
}
