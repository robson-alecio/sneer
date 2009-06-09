package snapps.watchme;

import java.awt.image.BufferedImage;

import sneer.brickness.Brick;
import sneer.brickness.PublicKey;
import sneer.pulp.events.EventSource;

@Brick
public interface WatchMe {

	void startShowingMyScreen();
	void stopShowingMyScreen();
	
	EventSource<BufferedImage> screenStreamFor(PublicKey publicKey);
	
}
