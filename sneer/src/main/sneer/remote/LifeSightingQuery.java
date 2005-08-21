package sneer.remote;

import sneer.life.LifeView;

class LifeSightingQuery implements Query<LifeSighting> {

	public LifeSighting executeOn(LifeView lifeView) {
		return new LifeSighting(lifeView);
	}

	LifeSightingQuery() {}  //Required by XStream to run on JVMs other than Sun's.

}
