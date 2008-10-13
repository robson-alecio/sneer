package sneer.pulp.own.avatar.tests;

import static wheel.io.ui.graphics.Images.getImage;
import static wheel.io.ui.graphics.Images.isSameImage;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.own.avatar.OwnAvatarKeeper;
import sneer.skin.image.ImageFactory;
import wheel.lang.exceptions.Hiccup;

public class OwnAvatarKeeperTest extends TestThatIsInjected {

	@Inject
	private static OwnAvatarKeeper _avatarKeeper;
	
	@Inject
	private static ImageFactory _imageFactory;

	private BufferedImage loadImage(String fileName) throws Hiccup {
		return _imageFactory.createBufferedImage(getImage(getClass().getResource(fileName)));
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

}
