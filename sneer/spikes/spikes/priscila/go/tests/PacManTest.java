package spikes.priscila.go.tests;

import junit.framework.TestCase;

public class PacManTest extends TestCase {

	private interface Pawn {

		void cima();

		void direita();

		void baixo();

		void esquerda();

	}

	private Pawn pacman;
	private Pawn ghost3;
	private Pawn ghost2;

	public void testSimpleMovement() {

		setLabirinto(
			"      ",
			"   @  ",
			"      ",
			"      ",
			"    H ",
			"      ");
		
		pacman.cima();
		assertLabirinto(
			"   @  ",
			"      ",
			"      ",
			"      ",
			"    H ",
			"      ");

		pacman.direita();
		assertLabirinto(
			"    @ ",
			"      ",
			"      ",
			"      ",
			"    H ",
			"      ");

		pacman.cima();
		assertLabirinto(
			"      ",
			"      ",
			"      ",
			"      ",
			"    H ",
			"    @ ");

		pacman.cima();
		assertLabirinto(
			"      ",
			"      ",
			"      ",
			"      ",
			"    H ",
			"    @ "
		);
	}
	
	public void testMorte() {
		
		setLabirinto(
			" 1 H  ",
			" 2 H  ",
			"     3",
			"HHH   ",
			"    @ ",
			" 4    ");
		
		pacman.cima();
		ghost2.baixo();
		ghost3.baixo();
		assertLabirinto(
			" 1 H  ",
			"   H  ",
			" 2    ",
			"HHH @3",
			"      ",
			" 4    ");
		
		ghost3.esquerda();
		assertLabirinto(
			" 1 H  ",
			"   H  ",
			" 2    ",
			"HHH X3",
			"      ",
			" 4    ");
		assertSound("SomDaMorte.wav");
		
		tick();
		assertLabirinto(
			" 1 H  ",
			"   H  ",
			" 2 @  ",
			"HHH  3",
			"      ",
			" 4    "
		);
		
	}

	public void testRender() {
		setLabirinto(
			" 1 H  ",
			" 2 H  ",
			"     3",
			"HH %  ",
			"....@ ",
			" 4    "
		);
		assertRendering("PacmanScene1.bmp");
	}

	
	private void assertSound(@SuppressWarnings("unused")
			String string) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}
	
	private void assertRendering(String string) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	private void tick() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@SuppressWarnings("unused")
	private void assertLabirinto(
	String string, String string2, String string3,
			String string4, String string5, String string6) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@SuppressWarnings("unused")
	private void setLabirinto(String string, String string2, String string3,
			String string4, String string5, String string6) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

}
