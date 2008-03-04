package functionaltests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sneer.lego.Brick;

public class Freedom1Test {

	@Brick
	private SovereignParty _subject;
	
	@Test
	public void testOwnName() {
		changeNameTo("Klaus W");
		changeNameTo("Wuestefeld, Klaus");
		changeNameTo("Klaus Wuestefeld");
	}

	private void changeNameTo(String newName) {
		_subject.setOwnName(newName);
		assertEquals(newName, _subject.ownName());
	}

}
