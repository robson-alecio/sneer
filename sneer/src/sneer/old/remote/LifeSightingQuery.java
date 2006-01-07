package sneer.old.remote;

import sneer.old.life.LifeView;

class LifeSightingQuery implements Query<LifeCache> {

	public LifeCache executeOn(LifeView lifeView) {
		return new LifeCache(lifeView);
	}

	LifeSightingQuery() {}  //Required by XStream to run on JVMs other than Sun's.

	private static final long serialVersionUID = 1L;

}
