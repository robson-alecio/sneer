package snapps.watchme;

import java.awt.image.BufferedImage;

import sneer.kernel.container.Brick;
import sneer.kernel.container.PublicKey;
import wheel.reactive.EventSource;

public interface WatchMe extends Brick {

	void startShowingMyScreen();
	void stopShowingMyScreen();
	
	EventSource<BufferedImage> screenStreamFor(PublicKey publicKey);
	
}
