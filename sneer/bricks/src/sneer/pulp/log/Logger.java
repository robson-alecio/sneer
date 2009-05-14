package sneer.pulp.log;

import sneer.brickness.Brick;

@Brick
public interface Logger extends Worker {

	void setDelegate(Worker worker);
	void enterRobustMode();
	
}
