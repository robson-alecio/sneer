package sneer.pulp.retrier;

import sneer.hardware.cpu.exceptions.FriendlyException;
import sneer.hardware.cpu.exceptions.Hiccup;

public interface Task {

	void execute() throws FriendlyException, Hiccup;

}
