package sneer.pulp.log.workers.notifier;

import sneer.brickness.Brick;
import sneer.pulp.events.EventSource;
import sneer.pulp.log.Worker;

@Brick
public interface LogNotifier extends Worker{

	EventSource<String> loggedMessages();

}
