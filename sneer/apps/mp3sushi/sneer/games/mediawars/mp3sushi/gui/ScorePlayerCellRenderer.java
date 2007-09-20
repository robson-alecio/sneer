package sneer.games.mediawars.mp3sushi.gui;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import sneer.games.mediawars.mp3sushi.player.PlayerIdentification;


public class ScorePlayerCellRenderer extends javax.swing.DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public Component getListCellRendererComponent(JList list, Object playerObject, int index, boolean isSelected, boolean hasFocus) {
		PlayerIdentification player = (PlayerIdentification) playerObject;

		FlowLayout layout = new FlowLayout();
		JPanel panel = new JPanel(layout);
		layout.setAlignment(FlowLayout.LEFT);

		if (player == null) return panel;
		
		panel.add(new JLabel(player.getNick().currentValue()));
		panel.add(new JLabel(""+player.getTotalScore().output().currentValue()));

		return panel;
	}

}
