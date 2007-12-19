package spikes.priscila.go.tests;

import java.awt.Color;

import spikes.priscila.go.ToroidalGoBoard;
import junit.framework.TestCase;

public class GoTest extends TestCase {

	private ToroidalGoBoard _board;

	public void testWhiteCapture() {
		playToCapture(Color.white);
	}

	public void testBlackCapture() {
		_board.playStone(0,0);
		playToCapture(Color.black);
	}

	public void playToCapture(Color color) {
		_board.playStone(3,3);
		_board.playStone(3,4);
		_board.playStone(4,2);
		_board.playStone(4,3);
		_board.playStone(5,3);
		_board.playStone(5,4);

		assertTrue(_board.stoneAt(4, 3) == color);
		_board.playStone(4,4);
		assertTrue(_board.stoneAt(4, 3) == null);
		
		_board.playStone(4,5);
		_board.playStone(0,1);

		assertTrue(_board.stoneAt(4, 4) == _board.other(color));
		_board.playStone(4,3);
		assertTrue(_board.stoneAt(4, 4) == null);


	}
	

	@Override
	protected void setUp() throws Exception {
		_board = new ToroidalGoBoard(9);
	}



}
