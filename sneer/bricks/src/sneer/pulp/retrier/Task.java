package sneer.pulp.retrier;

import sneer.software.exceptions.FriendlyException;
import wheel.lang.exceptions.Hiccup;

public interface Task {

	void execute() throws FriendlyException, Hiccup;

}
