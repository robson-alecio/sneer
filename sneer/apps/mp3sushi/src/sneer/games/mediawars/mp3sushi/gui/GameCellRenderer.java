package sneer.games.mediawars.mp3sushi.gui;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import sneer.games.mediawars.mp3sushi.TheirsGame;


public class GameCellRenderer extends javax.swing.DefaultListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public Component getListCellRendererComponent(JList list, Object gameObject, int index, boolean isSelected, boolean hasFocus) {
		TheirsGame game = (TheirsGame) gameObject;

		FlowLayout layout = new FlowLayout();
		JPanel panel = new JPanel(layout);
		layout.setAlignment(FlowLayout.LEFT);

		if (game == null) return panel;
		
		panel.add(new JLabel(game.getPlayer().nick().currentValue()));
		panel.add(new JLabel((game.getStatus() != null) ? game.getStatus().output().currentValue() : "Unknown"));
		String summary = "";
		if ((game.getGameConfiguration()!= null) && (game.getGameConfiguration().output().currentValue()!= null))
			summary = game.getGameConfiguration().output().currentValue().getSummary();
		panel.add(new JLabel(summary));

		return panel;
	}

}
