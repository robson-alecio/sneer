package snapps.watchme.tests;

import java.awt.image.BufferedImage;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;

import snapps.watchme.WatchMe;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.skin.screenshotter.Screenshotter;
import wheel.lang.Omnivore;
import wheel.reactive.EventSource;

public class WatchMeTest {
	
	final Mockery _context = new JUnit4Mockery();
	
	private BufferedImage _screenObserved;
	
	@Test (timeout = 5000)
	public void watchMe() {
		
		final Screenshotter shotter = _context.mock(Screenshotter.class);
		
		final BufferedImage image1 = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
		final BufferedImage image2 = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
		final BufferedImage image3 = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
		
		_context.checking(new Expectations() {{
			final Sequence seq = _context.sequence("sequence");

			one(shotter).takeScreenshot(); will(returnValue(image1)); inSequence(seq);
			one(shotter).takeScreenshot(); will(returnValue(image2)); inSequence(seq);
			one(shotter).takeScreenshot(); will(returnValue(image3)); inSequence(seq);
		}});
		
		final Container container = ContainerUtils.newContainer(shotter);
		final WatchMe subject = container.produce(WatchMe.class);
		
		KeyManager keys = container.produce(KeyManager.class);
		PublicKey ownKey = keys.ownPublicKey();
		
		EventSource<BufferedImage> screens = subject.screenStreamFor(ownKey);
		screens.addReceiver(new Omnivore<BufferedImage>() { @Override public void consume(BufferedImage screen) {
			_screenObserved = screen;
		}});
		
		subject.startShowingMyScreen();
		Assert.assertEquals(_screenObserved, image1);

		Clock clock = container.produce(Clock.class);
		clock.advanceTime(500);
		Assert.assertEquals(_screenObserved, image2);

		clock.advanceTime(500);
		Assert.assertEquals(_screenObserved, image3);
		
		_context.assertIsSatisfied();
		
	}

}
