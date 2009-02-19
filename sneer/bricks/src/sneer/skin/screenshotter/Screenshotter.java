package sneer.skin.screenshotter;

import java.awt.image.BufferedImage;

import sneer.kernel.container.Brick;

import wheel.lang.exceptions.FriendlyException;
import wheel.lang.exceptions.Hiccup;

public interface Screenshotter extends Brick {

	BufferedImage takeScreenshot() throws FriendlyException, Hiccup;
	
}
