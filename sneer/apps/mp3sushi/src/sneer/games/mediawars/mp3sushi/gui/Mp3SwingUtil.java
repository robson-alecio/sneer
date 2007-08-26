package sneer.games.mediawars.mp3sushi.gui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Mp3SwingUtil {

	public static JPanel createLabelPlusField(String label, JComponent field) {
		JPanel guessPane = new JPanel();
		guessPane.setLayout(new BoxLayout(guessPane, BoxLayout.PAGE_AXIS));
		JLabel jLabel = new JLabel(label);
		jLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		guessPane.add(jLabel);
		guessPane.add(Box.createVerticalStrut(5));
		field.setAlignmentX(Component.LEFT_ALIGNMENT);
		guessPane.add(field);
		guessPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return guessPane;
	}

}
