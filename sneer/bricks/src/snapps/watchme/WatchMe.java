package snapps.watchme;

import java.awt.image.BufferedImage;

import sneer.brickness.Brick;
import sneer.brickness.PublicKey;
import sneer.pulp.events.EventSource;

public interface WatchMe extends Brick {

	void startShowingMyScreen();
	void stopShowingMyScreen();
	
	EventSource<BufferedImage> screenStreamFor(PublicKey publicKey);
	
}
