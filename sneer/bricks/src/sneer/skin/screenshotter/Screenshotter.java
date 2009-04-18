package sneer.skin.screenshotter;

import java.awt.image.BufferedImage;

import sneer.brickness.OldBrick;
import sneer.hardware.cpu.exceptions.FriendlyException;
import sneer.hardware.cpu.exceptions.Hiccup;

public interface Screenshotter extends OldBrick {

	BufferedImage takeScreenshot() throws FriendlyException, Hiccup;
	
}
