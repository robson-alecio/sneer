package sneer.pulp.retrier;

import wheel.lang.exceptions.FriendlyException;
import wheel.lang.exceptions.Hiccup;

public interface Task {

	void execute() throws FriendlyException, Hiccup;

}
