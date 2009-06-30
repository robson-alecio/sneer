package spikes.sneer.bricks.pulp.own.avatar.tests;

import static sneer.foundation.environments.Environments.my;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import org.junit.Test;

import sneer.bricks.hardware.cpu.exceptions.Hiccup;
import sneer.bricks.hardware.gui.images.Images;
import sneer.bricks.skin.image.ImageFactory;
import sneer.foundation.brickness.testsupport.BrickTest;
import spikes.sneer.bricks.pulp.own.avatar.OwnAvatarKeeper;

public class OwnAvatarKeeperTest extends BrickTest {

	private final OwnAvatarKeeper _avatarKeeper = my(OwnAvatarKeeper.class);
	
	private final ImageFactory _imageFactory = my(ImageFactory.class);

	private BufferedImage loadImage(String fileName) throws Hiccup {
		return _imageFactory.createBufferedImage(my(Images.class).getImage(getClass().getResource(fileName)));
	}

	@Test
	public void testImage() throws Hiccup {
		
		if (GraphicsEnvironment.isHeadless())
			return;

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

	private boolean isSameImage(BufferedImage image1, BufferedImage image2) {
		return my(Images.class).isSameImage(image1, image2);
	}

}
