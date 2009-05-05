package sneer.pulp.lang;

import java.util.Date;

import sneer.brickness.Brick;

@Brick
public interface FrozenTime {

	Date frozenDate();

	long frozenTimeMillis();

	void freezeForCurrentThread(long millis);

}