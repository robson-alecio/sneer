package functionaltests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import functionaltests.adapters.SneerSovereignCommunity;
import functionaltests.adapters.SneerSovereignParty;

import sneer.lego.Binder;
import sneer.lego.Brick;
import sneer.lego.impl.SimpleBinder;
import sneer.lego.tests.BrickTestSupport;

public class Freedom1Test extends BrickTestSupport {
	
	@Brick
	private SovereignCommunity community; 
	
	@Override
	protected Binder getBinder() {
		Binder binder = new SimpleBinder();
		binder.bind(SovereignParty.class).to(SneerSovereignParty.class);
		binder.bind(SovereignCommunity.class).to(SneerSovereignCommunity.class);
		return binder;
	}

	@Test
	public void testOwnName() {
		
		//if (!TestDashboard.newTestsShouldRun()) return;
		
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
