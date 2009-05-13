package sneer.pulp.log;

import sneer.brickness.Brick;

@Brick
public interface Logger extends Worker {

	void filter(Filter filter);
	void delegate(Worker worker);
	void enterRobustMode();
	
}
