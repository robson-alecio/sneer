package wheel.io.ui.graphics;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.net.URL;
import java.util.Arrays;

import wheel.lang.exceptions.Hiccup;
import wheel.testutil.Profiler;

public class Images {

	private static Profiler _isSameImageProfiler = new Profiler("Images.isSameImage()");

	public static Image getImage(URL url) {
		return Toolkit.getDefaultToolkit().getImage(url);
	}
	
	static public boolean isSameImage(BufferedImage image1, BufferedImage image2) {
		try {
			_isSameImageProfiler.enter();
			return doIsSameImage(image1, image2);
		} finally {
			_isSameImageProfiler.exit();
		}
	}

	private static boolean doIsSameImage(BufferedImage image1, BufferedImage image2) {
		if (image2.getWidth() != image1.getWidth()) return false;
		if (image2.getHeight() != image1.getHeight()) return false;
		
		return Arrays.equals(pixels(image1), pixels(image2));
	}
	
	/** @throws Hiccup */
	private static int[] pixels(BufferedImage image) {
		PixelGrabber result = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), true);
		try {
			result.grabPixels();
		} catch (InterruptedException e) {
			throw new IllegalStateException();
		}

		return (int[])result.getPixels();
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
