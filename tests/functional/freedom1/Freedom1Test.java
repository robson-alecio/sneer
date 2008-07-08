package functional.freedom1;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import functional.SovereignFunctionalTest;
import functional.SovereignParty;


public abstract class Freedom1Test extends SovereignFunctionalTest {
	
	@Test (timeout = 1000)
	public void testOwnName() {
		SovereignParty me = _community.createParty("Klaus");
		changeNameTo(me, "Klaus W");
		changeNameTo(me, "Wuestefeld, Klaus");
		changeNameTo(me, "Klaus Wuestefeld");
	}

	private void changeNameTo(SovereignParty party, String newName) {
		party.setOwnName(newName);
		assertEquals(newName, party.ownName());
	}
}
