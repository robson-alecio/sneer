package snapps.watchme.tests;

import static sneer.commons.environments.Environments.my;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.junit.Test;

import snapps.watchme.WatchMe;
import sneer.brickness.PublicKey;
import sneer.brickness.testsupport.BrickTest;
import sneer.brickness.testsupport.BrickTestRunner;
import sneer.brickness.testsupport.Contribute;
import sneer.commons.environments.Environment;
import sneer.hardware.cpu.exceptions.Hiccup;
import sneer.hardware.cpu.lang.Consumer;
import sneer.hardware.gui.images.Images;
import sneer.pulp.clock.Clock;
import sneer.pulp.events.EventSource;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.reactive.Signals;
import sneer.pulp.threads.Threads;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.image.ImageFactory;
import sneer.skin.screenshotter.Screenshotter;

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
		WatchMe subject2 = container2.provide(WatchMe.class);

		PublicKey key = _keys.ownPublicKey();
		
		EventSource<BufferedImage> screens = subject2.screenStreamFor(key);

		@SuppressWarnings("unused") Object referenceToAvoidGc = my(Signals.class).receive(new Consumer<BufferedImage>() {@Override public void consume(BufferedImage screen) {
			_screenObserved.set(screen);
		}}, screens);

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
