package functional.freedom1;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import functional.SovereignFunctionalTestBase;
import functional.SovereignParty;


public abstract class Freedom1TestBase extends SovereignFunctionalTestBase {
	
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
