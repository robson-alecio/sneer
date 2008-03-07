package functionaltests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sneer.lego.Brick;
import sneer.lego.tests.BrickTestSupport;

public class Freedom1Test extends BrickTestSupport {

	
	@Brick
	private SovereignCommunity community; 
	
	@Test
	public void testOwnName() {
		
		if (!TestDashboard.newTestsShouldRun()) return;
		
		SovereignParty subject = community.createParty("Klaus");
		changeNameTo(subject, "Klaus W");
		changeNameTo(subject, "Wuestefeld, Klaus");
		changeNameTo(subject, "Klaus Wuestefeld");
	}

	private void changeNameTo(SovereignParty subject, String newName) {
		subject.setOwnName(newName);
		assertEquals(newName, subject.ownName());
	}

}
