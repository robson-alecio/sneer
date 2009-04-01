package sneer.pulp.logging.robust.impl;

import sneer.pulp.logging.Logger;
import sneer.pulp.logging.robust.RobustLogger;
import static sneer.commons.environments.Environments.my;

public class RobustLoggerImpl implements RobustLogger {

	{
		my(Logger.class).enterRobustMode();
	}
	
}
