package sneer.skin.screenshotter;

import java.awt.image.BufferedImage;

import sneer.brickness.OldBrick;
import sneer.software.exceptions.FriendlyException;
import wheel.lang.exceptions.Hiccup;

public interface Screenshotter extends OldBrick {

	BufferedImage takeScreenshot() throws FriendlyException, Hiccup;
	
}
