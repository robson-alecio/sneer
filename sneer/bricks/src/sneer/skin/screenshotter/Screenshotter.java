package sneer.skin.screenshotter;

import java.awt.image.BufferedImage;

import sneer.brickness.Brick;
import sneer.hardware.cpu.exceptions.FriendlyException;
import sneer.hardware.cpu.exceptions.Hiccup;

@Brick
public interface Screenshotter {

	BufferedImage takeScreenshot() throws FriendlyException, Hiccup;
	
}
