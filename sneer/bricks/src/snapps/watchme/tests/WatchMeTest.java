package snapps.watchme.tests;

import static wheel.io.ui.graphics.Images.getImage;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Ignore;
import org.junit.Test;

import snapps.watchme.WatchMe;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.image.ImageFactory;
import sneer.skin.screenshotter.Screenshotter;
import wheel.io.ui.graphics.Images;
import wheel.lang.Threads;
import wheel.lang.exceptions.Hiccup;
import wheel.reactive.EventSource;
import wheel.reactive.impl.Receiver;

public class WatchMeTest extends TestThatIsInjected {
	
	@Inject
	private static ImageFactory _imageFactory;
	@Inject
	private static Clock _clock;
	@Inject
	private static TupleSpace _sharedSpace;

	@Inject
	private static WatchMe _subject;

	final private Mockery _context = new JUnit4Mockery();
	final private Screenshotter _shotter = _context.mock(Screenshotter.class);
	
	private AtomicReference<BufferedImage> _screenObserved = new AtomicReference<BufferedImage>(null);

	@Override
	protected Object[] getBindings() {
		return new Object[] { _shotter };
	}

	@Ignore
	@Test //(timeout = 7000)
	public void watchMe() throws Exception, Hiccup {
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
		WatchMe subject2 = container2.produce(WatchMe.class);


		
		// Fix: This is an ugly workaround for the fact that class Tuple will be shared
		// among both containers but will be injected with the KeyManager of the
		// last created container. The correct behaviour is to use the ownPublicKey
		// from the first container.
		PublicKey key = container2.produce(KeyManager.class).ownPublicKey();

		
		
		EventSource<BufferedImage> screens = subject2.screenStreamFor(key);
		@SuppressWarnings("unused")
		Receiver<BufferedImage> receiverToAvoidGC = new Receiver<BufferedImage>(screens){@Override public void consume(BufferedImage screen) {
			_screenObserved.set(screen);
			System.out.println(System.nanoTime());
		}};
		
		_subject.startShowingMyScreen();
		waitForImage(image1);

		_clock.advanceTime(500);
		waitForImage(image2);

		_clock.advanceTime(500);
		waitForImage(image3);
		
		_context.assertIsSatisfied();
		
	}

	private void waitForImage(BufferedImage expected) {
		int i = 0;
		while (true) {
			System.out.println("Waiting " + i++);
			BufferedImage observed = _screenObserved.get();
			if (observed != null)
				if (Images.isSameImage(expected, observed)) return;
			Threads.sleepWithoutInterruptions(10);
		}
	}

	private BufferedImage loadImage(String fileName) throws Hiccup {
		return _imageFactory.createBufferedImage(getImage(getClass().getResource(fileName)));
	}

}
