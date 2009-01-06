package spikes.priscila.go.tests;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import spikes.priscila.go.GoBoard;
import spikes.priscila.go.ToroidalGoBoard;
import wheel.lang.exceptions.NotImplementedYet;

public class GoTest extends Assert {

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
		GoBoard board = new ToroidalGoBoard(setup);
		
		assertTrue(board.stoneAt(4, 3) != null);
		board.playStone(4,4);
		assertTrue(board.stoneAt(4, 3) == null);
		
		board.playStone(4,5);
		board.playStone(0,1);
		
		assertTrue(board.stoneAt(4, 4) != null);
		board.playStone(4,3);
		assertTrue(board.stoneAt(4, 4) == null);
	}

	@Test
	public void testSingleStoneCapture() {
		GoBoard board = new ToroidalGoBoard(9);
		
		board.playStone(4, 2);
		board.playStone(4, 3);
		board.playStone(3, 3);
		board.playStone(3, 4);
		board.playStone(5, 3);
		board.playStone(5, 4);

		assertTrue(board.stoneAt(4, 3) != null);
		board.playStone(4,4);
		assertTrue(board.stoneAt(4, 3) == null);
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
		GoBoard board = new ToroidalGoBoard(setup);
		
		board.playStone(5, 5);
		
		assertEquals(board.printOut(),
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
		GoBoard board = new ToroidalGoBoard(setup);
		assertFalse(board.canPlayStone(5, 4));
		assertTrue(board.stoneAt(5, 4) == null);
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
		GoBoard board = new ToroidalGoBoard(setup);
		assertTrue(board.canPlayStone(4, 3));
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
		GoBoard board = new ToroidalGoBoard(setup);
		assertTrue(board.canPlayStone(4, 3));
		board.playStone(4, 3);
		assertFalse(board.canPlayStone(4, 2));
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
		GoBoard board = new ToroidalGoBoard(setup);
		
		board.playStone(4, 3);
		assertEquals(board.printOut(),
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
	}
}
