package sneer.remote;

import sneer.life.LifeView;

class LifeSightingQuery implements Query<LifeSighting> {

	public LifeSighting executeOn(LifeView lifeView) {
		return new LifeSighting(lifeView);
	}

	private static final long serialVersionUID = 1L;

}
