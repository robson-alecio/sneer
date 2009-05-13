package sneer.pulp.log.robust.impl;

import sneer.pulp.log.Logger;
import sneer.pulp.log.robust.RobustLogger;
import static sneer.commons.environments.Environments.my;

public class RobustLoggerImpl implements RobustLogger {

	{
		my(Logger.class).enterRobustMode();
	}
	
}
