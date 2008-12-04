package snapps.watchme.tests;

import static wheel.io.ui.graphics.Images.getImage;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import snapps.watchme.WatchMe;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.image.ImageFactory;
import sneer.skin.screenshotter.Screenshotter;
import tests.TestThatIsInjected;
import wheel.io.ui.graphics.Images;
import wheel.lang.Threads;
import wheel.lang.exceptions.Hiccup;
import wheel.reactive.EventSource;
import wheel.reactive.impl.Receiver;

public class WatchMeTest extends TestThatIsInjected {
	
	@Inject
	private static ImageFactory _imageFactory;
	@Inject
	private static KeyManager _keys;
	@Inject
	private static Clock _clock;
	@Inject
	private static TupleSpace _sharedSpace;

	@Inject
	private static WatchMe _subject;

	final private Mockery _context = new JUnit4Mockery();
	final private Screenshotter _shotter = _context.mock(Screenshotter.class);
	
	private AtomicReference<BufferedImage> _screenObserved = new AtomicReference<BufferedImage>(null);

	@Test
	public void watchMe() throws Exception, Hiccup {
		
		if (GraphicsEnvironment.isHeadless())
			return;
		
		final BufferedImage image1 = loadImage("screen1.png");
		final BufferedImage image2 = loadImage("screen2.png");
		final BufferedImage image3 = loadImage("screen3.png");
		
		_context.checking(new Expectations() {{
			final Sequence seq = _context.sequence("sequence");

			one(_shotter).takeScreenshot(); will(returnValue(image1)); inSequence(seq);
			one(_shotter).takeScreenshot(); will(returnValue(image2)); inSequence(seq);
			one(_shotter).takeScreenshot(); will(returnValue(image3)); inSequence(seq);
		}});

		Container container2 = ContainerUtils.newContainer(_sharedSpace); 
		WatchMe subject2 = container2.provide(WatchMe.class);

		PublicKey key = _keys.ownPublicKey();
		
		EventSource<BufferedImage> screens = subject2.screenStreamFor(key);
		@SuppressWarnings("unused")
		Receiver<BufferedImage> receiverToAvoidGC = new Receiver<BufferedImage>(screens){@Override public void consume(BufferedImage screen) {
			_screenObserved.set(screen);
		}};
		
		_subject.startShowingMyScreen();
		waitForImage(image1);

		_clock.advanceTime(1000);
		waitForImage(image2);

		_clock.advanceTime(1000);
		waitForImage(image3);

		_context.assertIsSatisfied();
		
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
		int i = 0;
		while (true) {
			BufferedImage observed = _screenObserved.get();
			if (observed != null)
				if (Images.isSameImage(expected, observed)) return;
			
			if (i++ == 100) giveUp(expected, observed);
			
			Threads.sleepWithoutInterruptions(300); //Optimize Use wait/notify
		}
	}

	private void giveUp(BufferedImage expected, BufferedImage observed) {
		if (observed == null)
			fail("Observed image was null.");
		
		System.err.println("Expected image not received. Opening for comparison. Closing in 30 sec...");
		showImage("Expected", expected);
		showImage("Observed", observed);
		Threads.sleepWithoutInterruptions(30000);
		fail();
	}

	private BufferedImage loadImage(String fileName) throws Hiccup {
		return _imageFactory.createBufferedImage(getImage(getClass().getResource(fileName)));
	}

}
