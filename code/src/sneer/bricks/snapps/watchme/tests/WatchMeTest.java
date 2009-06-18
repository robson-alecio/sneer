package sneer.bricks.snapps.watchme.tests;

import static sneer.foundation.environments.Environments.my;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.junit.Test;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.exceptions.Hiccup;
import sneer.bricks.hardware.gui.images.Images;
import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.keymanager.KeyManager;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.threads.Threads;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.skin.image.ImageFactory;
import sneer.bricks.skin.screenshotter.Screenshotter;
import sneer.bricks.snapps.watchme.WatchMe;
import sneer.foundation.brickness.PublicKey;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.BrickTestRunner;
import sneer.foundation.brickness.testsupport.Contribute;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.EnvironmentUtils;
import sneer.foundation.lang.Consumer;

public class WatchMeTest extends BrickTest {
	
	@Contribute
	final private Screenshotter _shotter = mock(Screenshotter.class);
	private final TupleSpace _sharedSpace = my(TupleSpace.class);
	
	private final ImageFactory _imageFactory = my(ImageFactory.class);
	private final KeyManager _keys = my(KeyManager.class);
	private final Clock _clock = my(Clock.class);
	private final WatchMe _subject = my(WatchMe.class);
	
	private AtomicReference<BufferedImage> _screenObserved = new AtomicReference<BufferedImage>(null);

	@Test
	public void watchMe() throws Exception, Hiccup {
		
		if (GraphicsEnvironment.isHeadless())
			return;
		
		final BufferedImage image1 = loadImage("screen1.png");
		final BufferedImage image2 = loadImage("screen2.png");
		final BufferedImage image3 = loadImage("screen3.png");
		
		checking(new Expectations() {{
			final Sequence seq = sequence("sequence");

			one(_shotter).takeScreenshot(); will(returnValue(image1)); inSequence(seq);
			one(_shotter).takeScreenshot(); will(returnValue(image2)); inSequence(seq);
			one(_shotter).takeScreenshot(); will(returnValue(image3)); inSequence(seq);
		}});

		Environment container2 = my(BrickTestRunner.class).newTestEnvironment(_sharedSpace); 
		WatchMe subject2 = EnvironmentUtils.retrieveFrom(container2, WatchMe.class);

		PublicKey key = _keys.ownPublicKey();
		
		EventSource<BufferedImage> screens = subject2.screenStreamFor(key);

		@SuppressWarnings("unused") Object referenceToAvoidGc = my(Signals.class).receive(screens, new Consumer<BufferedImage>() {@Override public void consume(BufferedImage screen) {
			_screenObserved.set(screen);
		}});

		_subject.startShowingMyScreen();
		waitForImage(image1);

		_clock.advanceTime(1000);
		waitForImage(image2);

		_clock.advanceTime(1000);
		waitForImage(image3);
	}

	private void showImage(String title, final BufferedImage image) {
		JFrame frame = new JFrame(title);
		frame.setBounds(0,0, 1100, 800);
		
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(image));
		frame.getContentPane().add(label);
		
		frame.setVisible(true);
	}

	private void waitForImage(BufferedImage expected) {
		_sharedSpace.waitForAllDispatchingToFinish();

		int i = 0;
		while (true) {
			BufferedImage observed = _screenObserved.get();
			if (observed != null)
				if (my(Images.class).isSameImage(expected, observed)) return;
			
			if (i++ == 100) giveUp(expected, observed);
			
			my(Threads.class).sleepWithoutInterruptions(300); //Optimize Use wait/notify
		}
	}

	private void giveUp(BufferedImage expected, BufferedImage observed) {
		if (observed == null)
			fail("Observed image was null.");
		
		System.err.println("Expected image not received. Opening for comparison. Closing in 30 sec...");
		showImage("Expected", expected);
		showImage("Observed", observed);
		my(Threads.class).sleepWithoutInterruptions(30000);
		fail();
	}

	private BufferedImage loadImage(String fileName) throws Hiccup {
		return _imageFactory.createBufferedImage(my(Images.class).getImage(getClass().getResource(fileName)));
	}

}
