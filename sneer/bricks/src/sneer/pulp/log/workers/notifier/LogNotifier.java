package sneer.pulp.log.workers.notifier;

import sneer.brickness.Brick;
import sneer.pulp.events.EventSource;
import sneer.pulp.log.LogWorker;

@Brick
public interface LogNotifier extends LogWorker{

	EventSource<String> loggedMessages();

}
