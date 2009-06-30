package sneer.tests.freedom1;

import org.junit.Test;

import sneer.tests.SovereignFunctionalTestBase;
import sneer.tests.SovereignParty;


public abstract class Freedom1TestBase extends SovereignFunctionalTestBase {
	
	@Test (timeout = 3000)
	public void testOwnName() {
		SovereignParty party = createParty("Neide");
		changeNameTo(party, "Neide S");
		changeNameTo(party, "Silva, Neide");
		changeNameTo(party, "Neide Silva");
	}

	@Test (timeout = 3000)
	public void testPublicKey() {
//		SovereignParty party = createParty("Neide");
//		Sneer1024 seal = party.seal();
//		byte[] publicKey = party.publicKey();
//		assertArrayEquals(my(Digester.class).digest(publicKey), seal);
	}

	
	
	private void changeNameTo(SovereignParty party, String newName) {
		party.setOwnName(newName);
		assertEquals(newName, party.ownName());
	}
}
