package sneer.pulp.retrier;

import wheel.lang.exceptions.FriendlyException;

public interface Task {

	void execute() throws FriendlyException;

}
