package sneer.bricks.hardware.gui.images.impl;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.net.URL;
import java.util.Arrays;

import sneer.bricks.hardware.cpu.exceptions.Hiccup;
import sneer.bricks.hardware.gui.images.Images;


class ImagesImpl implements Images {

	@Override
	public Image getImage(URL url) {
		return Toolkit.getDefaultToolkit().getImage(url);
	}
	
	@Override
	public boolean isSameImage(BufferedImage image1, BufferedImage image2) {
			if (image2.getWidth() != image1.getWidth()) return false;
			if (image2.getHeight() != image1.getHeight()) return false;
			
			return Arrays.equals(pixels(image1), pixels(image2));
	}

	/** @throws Hiccup */
	@Override
	public int[] pixels(BufferedImage image) {
		PixelGrabber result = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), true);
		try {
			result.grabPixels();
		} catch (InterruptedException e) {
			throw new IllegalStateException();
		}

		return (int[])result.getPixels();
	}

	@Override
	public BufferedImage copy(BufferedImage original) {
		return new BufferedImage(
			original.getColorModel(),
			original.copyData(null),
			original.isAlphaPremultiplied(),
			null
		);
	}

}
