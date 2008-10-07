package wheel.io.ui.graphics;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;

public class Images {

	public static Image getImage(URL url) {
		return Toolkit.getDefaultToolkit().getImage(url);
	}
	
	static public boolean isSameImage(BufferedImage image1, BufferedImage image2) {
		if (image1.getWidth() != image2.getWidth()) return false;
		if (image1.getHeight() != image2.getHeight()) return false;
		
		for (int x = 0; x < image1.getWidth(); x++)
			for (int y = 0; y < image1.getHeight(); y++)
				if (image1.getRGB(x, y) != image2.getRGB(x, y))
					return false;

		return true;
	}
	
	static public BufferedImage copy(BufferedImage original) {
		return new BufferedImage(
			original.getColorModel(),
			original.copyData(null),
			original.isAlphaPremultiplied(),
			null
		);
	}

}
