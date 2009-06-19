package spikes.priscila.go.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import sneer.bricks.pulp.reactive.Register;
import spikes.priscila.go.Move;
import spikes.priscila.go.GoBoard.StoneColor;

public class GoFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final StoneColor _side;

	public GoFrame(Register<Move> _move, StoneColor side, int horizontalPosition) {
		
		_side = side;
		setTitle("Go - " + _side.name());	  
	    setResizable(true);
	    setBounds(horizontalPosition, 0, 500, 575);
	    setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addComponentPanel(_move);	
	}

	private void addComponentPanel(Register<Move> move) {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		GoBoardPanel goBoardPanel=new GoBoardPanel(move, _side);
		contentPane.add(goBoardPanel, BorderLayout.CENTER);
		
		JPanel goEastPanel = new JPanel();
		
		goEastPanel.setLayout(new FlowLayout());
		goEastPanel.add(new GoScorePanel(goBoardPanel.countCapturedBlack(),
				goBoardPanel.countCapturedWhite()));
		
		JSeparator space= new JSeparator(SwingConstants.VERTICAL);
		space.setPreferredSize(new Dimension(30,0));
		
		goEastPanel.add(space);
		goEastPanel.add(new GoMovesPanel(goBoardPanel));
				
		contentPane.add(goEastPanel, BorderLayout.SOUTH);
	}

}
