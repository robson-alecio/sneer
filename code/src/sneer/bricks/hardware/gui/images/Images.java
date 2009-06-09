package sneer.bricks.hardware.gui.images;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import sneer.foundation.brickness.Brick;

@Brick
public interface Images {

	Image getImage(URL url);

	boolean isSameImage(BufferedImage image1, BufferedImage image2);
	int[] pixels(BufferedImage image);
	BufferedImage copy(BufferedImage original);

}
