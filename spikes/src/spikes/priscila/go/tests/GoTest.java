package spikes.priscila.go.tests;

import org.junit.Ignore;
import org.junit.Test;

import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.commons.lang.exceptions.NotImplementedYet;
import spikes.priscila.go.GoBoard;
import spikes.priscila.go.ToroidalGoBoard;

public class GoTest extends BrickTest {

	private GoBoard _board;
	
	@Test
	public void testSingleStoneCaptureNew() {
		String[] setup = new String[]{
			    "+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + x + + + +",
				"+ + + x o x + + +",
				"+ + + o + o + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +"};
		_board = new ToroidalGoBoard(setup);
		
		assertTrue(_board.stoneAt(4, 3) != null);
		_board.playStone(4,4);
		assertTrue(_board.stoneAt(4, 3) == null);
		assertCaptured(0, 1);
		
		_board.playStone(4,5);
		_board.playStone(0,1);
		
		assertTrue(_board.stoneAt(4, 4) != null);
		_board.playStone(4,3);
		assertTrue(_board.stoneAt(4, 4) == null);
		assertCaptured(1, 1);
	}

	private void assertCaptured(int black, int white) {
		assertSame(black, _board.blackCapturedCount().currentValue());
		assertSame(white, _board.whiteCapturedCount().currentValue());
	}

	@Test
	public void testSingleStoneCapture() {
		_board = new ToroidalGoBoard(9);
		
		_board.playStone(4, 2);
		_board.playStone(4, 3);
		_board.playStone(3, 3);
		_board.playStone(3, 4);
		_board.playStone(5, 3);
		_board.playStone(5, 4);

		assertTrue(_board.stoneAt(4, 3) != null);
		_board.playStone(4,4);
		assertTrue(_board.stoneAt(4, 3) == null);
	}

	@Test
	public void testBigGroupCapture() {
		String[] setup = new String[]{
			    "+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + x x + + +",
				"+ + + x o o x + +",
				"+ + + + x o x + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +"};
		_board = new ToroidalGoBoard(setup);
		
		_board.playStone(5, 5);
		
		assertEquals(_board.printOut(),
		    " + + + + + + + + +\n" +
			" + + + + + + + + +\n" +
			" + + + + x x + + +\n" +
			" + + + x + + x + +\n" +
			" + + + + x + x + +\n" +
			" + + + + + x + + +\n" +
			" + + + + + + + + +\n" +
			" + + + + + + + + +\n" +
			" + + + + + + + + +\n"
		);
		assertCaptured(0, 3);
	}
	
	@Test
	public void testSuicide() {
		String[] setup = new String[] {
			    "+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + o o + + +",
				"+ + + o x x o + +",
				"+ + + + o + o + +",
				"+ + + + + o + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +"};
		_board = new ToroidalGoBoard(setup);
		assertFalse(_board.canPlayStone(5, 4));
		assertTrue(_board.stoneAt(5, 4) == null);
	}
	
	@Test
	public void testKillOtherFirst() {
		String[] setup = new String[]{
			    "+ + + + + + + + +",
				"+ + + + x + + + +",
				"+ + + x o x + + +",
				"+ + + o + o + + +",
				"+ + + + o + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +"};
		_board = new ToroidalGoBoard(setup);
		assertTrue(_board.canPlayStone(4, 3));
	}
	
	@Test
	public void testKo() {
		String[] setup = new String[]{
			    "+ + + + + + + + +",
				"+ + + + x + + + +",
				"+ + + x o x + + +",
				"+ + + o + o + + +",
				"+ + + + o + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +"};
		_board = new ToroidalGoBoard(setup);
		assertTrue(_board.canPlayStone(4, 3));
		_board.playStone(4, 3);
		assertFalse(_board.canPlayStone(4, 2));
	}

	
	@Test
	@Ignore
	public void testKoWithPass() {
		throw new NotImplementedYet();
	}
	
	@Test
	public void testMultipleGroupKill() {
		String[] setup = new String[]{
			    "+ + + + + + + + +",
				"+ + + + x + + + +",
				"+ + + x o x + + +",
				"+ + x o + + + + +",
				"+ + + x + + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +",
				"+ + + + + + + + +"};
		_board = new ToroidalGoBoard(setup);
		
		_board.playStone(4, 3);
		assertEquals(_board.printOut(),
			 	" + + + + + + + + +\n"+
				" + + + + x + + + +\n"+
				" + + + x + x + + +\n"+
				" + + x + x + + + +\n"+
				" + + + x + + + + +\n"+
				" + + + + + + + + +\n"+
				" + + + + + + + + +\n"+
				" + + + + + + + + +\n"+
				" + + + + + + + + +\n"
		);
		assertCaptured(0, 2);
	}
}
