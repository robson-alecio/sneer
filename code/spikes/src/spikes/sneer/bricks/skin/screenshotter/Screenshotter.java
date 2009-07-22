package spikes.sneer.bricks.skin.screenshotter;

import java.awt.image.BufferedImage;

import sneer.bricks.hardware.cpu.exceptions.Hiccup;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.exceptions.FriendlyException;

@Brick
public interface Screenshotter {

	BufferedImage takeScreenshot() throws FriendlyException, Hiccup;
	
}
