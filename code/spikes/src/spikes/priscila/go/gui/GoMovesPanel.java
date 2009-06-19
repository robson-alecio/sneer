package spikes.priscila.go.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GoMovesPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GoMovesPanel(final GoBoardPanel goBoardPanel) {
			
		JButton btnPassar= new JButton();
		btnPassar.setText("Pass");
		
		JButton btnDesistir= new JButton();
		btnDesistir.setText("Resign");
		 
		add(btnPassar);
		add(btnDesistir);
		
		btnPassar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				goBoardPanel.passTurn();
			}
			
		});
	
		setVisible(true);

	}

}
