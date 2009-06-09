package sneer.bricks.snapps.watchme;

import java.awt.image.BufferedImage;

import sneer.bricks.pulp.events.EventSource;
import sneer.foundation.brickness.Brick;
import sneer.foundation.brickness.PublicKey;

@Brick
public interface WatchMe {

	void startShowingMyScreen();
	void stopShowingMyScreen();
	
	EventSource<BufferedImage> screenStreamFor(PublicKey publicKey);
	
}
