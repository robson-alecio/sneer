package sneer.skin.screenshotter;

import java.awt.image.BufferedImage;

import sneer.brickness.Brick;
import wheel.lang.exceptions.FriendlyException;
import wheel.lang.exceptions.Hiccup;

public interface Screenshotter extends Brick {

	BufferedImage takeScreenshot() throws FriendlyException, Hiccup;
	
}
