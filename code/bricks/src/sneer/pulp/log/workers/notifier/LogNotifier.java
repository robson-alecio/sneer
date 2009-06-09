package sneer.pulp.log.workers.notifier;

import sneer.brickness.Brick;
import sneer.pulp.events.EventSource;

@Brick
public interface LogNotifier {

	EventSource<String> loggedMessages();

}
