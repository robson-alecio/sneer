package sneer.bricks.pulp.retrier;

import sneer.bricks.hardware.cpu.exceptions.Hiccup;
import sneer.foundation.lang.exceptions.FriendlyException;

public interface Task {

	void execute() throws FriendlyException, Hiccup;

}
