package functionaltests.adapters;

import sneer.lego.Binder;
import sneer.lego.impl.SimpleBinder;
import functionaltests.Freedom1Test;
import functionaltests.SovereignCommunity;
import functionaltests.SovereignParty;

public class SneerFreedom1Test extends Freedom1Test {

	@Override
	protected Binder getBinder() {
		Binder binder = new SimpleBinder();
		binder.bind(SovereignParty.class).to(SneerSovereignParty.class);
		binder.bind(SovereignCommunity.class).to(SneerSovereignCommunity.class);
		return binder;
	}

}
