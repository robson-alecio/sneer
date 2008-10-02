package snapps.watchme;

import java.awt.image.BufferedImage;

import sneer.pulp.keymanager.PublicKey;
import wheel.reactive.EventSource;

public interface WatchMe {

	void startShowingMyScreen();
	void stopShowingMyScreen();
	
	EventSource<BufferedImage> screenStreamFor(PublicKey publicKey);
	
}
