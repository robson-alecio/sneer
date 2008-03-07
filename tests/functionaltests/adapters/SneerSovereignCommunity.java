package functionaltests.adapters;

import sneer.lego.Brick;
import sneer.lego.Container;
import functionaltests.SovereignCommunity;
import functionaltests.SovereignParty;

public class SneerSovereignCommunity implements SovereignCommunity {

	@Brick
	private Container container;
	
	@Override
	public SovereignParty createParty(String name) {
		SovereignParty party = container.create(SovereignParty.class);
		party.setOwnName(name);
		return party;
	}


}
