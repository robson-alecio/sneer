package sneerapps.giventake.tests.impl;

import sneer.bricks.mesh.Me;
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
	static private Me _me;
	
	public void advertise(String title, String description) {
		_gnt.advertise(_thingHome.create(title, description));
	}

	public SetSignal<Thing> search(String tags) {
		return _gnt.firstLevelRemoteSearch(tags);
	}

	@Override
	public void connectTo(GiveNTakeUser peer) {
		registerGNTMock(peer);
		peer.registerGNTMock(this);
	}

	@Override
	public void registerGNTMock(GiveNTakeUser peer) {
		peer.addCounterpart(_gnt);
	}

	@Override
	public void addCounterpart(GiveNTake gnt) {
		((MeMock)_me).addCounterpart(gnt);
	}


}
