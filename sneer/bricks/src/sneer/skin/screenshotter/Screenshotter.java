package sneer.skin.screenshotter;

import java.awt.image.BufferedImage;

import wheel.lang.exceptions.FriendlyException;
import wheel.lang.exceptions.Hiccup;

public interface Screenshotter {

	BufferedImage takeScreenshot() throws FriendlyException, Hiccup;
	
}
