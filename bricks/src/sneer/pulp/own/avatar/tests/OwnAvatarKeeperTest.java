package sneer.pulp.own.avatar.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.own.avatar.OwnAvatarKeeper;
import sneer.skin.image.ImageFactory;
import wheel.graphics.Images;

public class OwnAvatarKeeperTest extends TestThatIsInjected {

	@Inject
	private static OwnAvatarKeeper _avatarKeeper;
	
	@Inject
	private static ImageFactory _imageFactory;

	private BufferedImage loadImage(String fileName) throws Exception {
		return _imageFactory.createBufferedImage(Images.getImage(getClass().getResource(fileName)));
	}

	@Test
	public void testImage() throws Exception {

		BufferedImage toTest;
		BufferedImage black = loadImage("black.png");
		BufferedImage white = loadImage("white.png");

		_avatarKeeper.avatarSetter().consume(black);
		toTest = _imageFactory.createBufferedImage(_avatarKeeper.avatar(20).currentValue());
		assertTrue(isSameImage(black, toTest));
		
		_avatarKeeper.avatarSetter().consume(white);
		toTest = _imageFactory.createBufferedImage(_avatarKeeper.avatar(20).currentValue());
		assertFalse(isSameImage(black, toTest));
		assertTrue(isSameImage(white, toTest));
		
		toTest = _imageFactory.createBufferedImage(_avatarKeeper.avatar(10).currentValue());
		assertFalse(isSameImage(white, toTest));
		assertTrue(isSameImage(_imageFactory.getScaledInstance(white, 10,10), toTest));
	}

	public boolean isSameImage(BufferedImage image1, BufferedImage image2) {
		if (image1.getWidth() != image2.getWidth()
				|| image1.getHeight() != image2.getHeight()) {
			return (false);
		}
		for (int x = 0; x < image1.getWidth(); x++) {
			for (int y = 0; y < image1.getHeight(); y++) {
				if (image1.getRGB(x, y) != image2.getRGB(x, y)) {
					return (false);
				}
			}
		}
		return (true);
	}

}
