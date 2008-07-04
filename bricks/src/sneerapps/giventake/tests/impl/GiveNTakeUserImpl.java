package sneerapps.giventake.tests.impl;

import sneer.bricks.mesh.Party;
import sneer.bricks.things.Thing;
import sneer.bricks.things.ThingHome;
import sneer.lego.Inject;
import sneerapps.giventake.GiveNTake;
import sneerapps.giventake.tests.GiveNTakeUser;
import sneerapps.giventake.tests.MeMock;
import wheel.reactive.sets.SetSignal;

class GiveNTakeUserImpl implements GiveNTakeUser {

	@Inject
	static private GiveNTake _gnt;
	@Inject
	static private ThingHome _thingHome;
	@Inject
	static private MeMock _me;
	
	public void advertise(String title, String description) {
		_gnt.advertise(_thingHome.create(title, description));
	}

	public SetSignal<Thing> search(String tags) {
		Party onlyPeer = _me.navigateTo(_me.contacts().currentGet(1));
		GiveNTake gntPeer = onlyPeer.brickProxyFor(GiveNTake.class);
		return gntPeer.search(tags);
	}

	@Override
	public void connectTo(GiveNTakeUser peer) {
		_me.connectTo(peer.yourMe());
	}

	@Override
	public MeMock yourMe() {
		return _me;
	}

}
