package sneer.bricks.skin.screenshotter;

import java.awt.image.BufferedImage;

import sneer.bricks.hardware.cpu.exceptions.FriendlyException;
import sneer.bricks.hardware.cpu.exceptions.Hiccup;
import sneer.foundation.brickness.Brick;

@Brick
public interface Screenshotter {

	BufferedImage takeScreenshot() throws FriendlyException, Hiccup;
	
}
