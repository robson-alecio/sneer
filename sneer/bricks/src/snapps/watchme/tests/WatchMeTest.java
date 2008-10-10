package snapps.watchme.tests;

import static wheel.io.ui.graphics.Images.getImage;

import java.awt.image.BufferedImage;

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
	private static KeyManager _keys;
	@Inject
	private static Clock _clock;
	@Inject
	private static TupleSpace _tupleSpace;

	@Inject
	private static WatchMe _subject;

	final private Mockery _context = new JUnit4Mockery();
	final private Screenshotter _shotter = _context.mock(Screenshotter.class);
	
	private volatile BufferedImage _screenObserved;

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
		
		Container container2 = ContainerUtils.newContainer(_tupleSpace, _keys); 
		WatchMe subject2 = container2.produce(WatchMe.class);

		EventSource<BufferedImage> screens = subject2.screenStreamFor(ownKey());
		@SuppressWarnings("unused")
		Receiver<BufferedImage> receiverToAvoidGC = new Receiver<BufferedImage>(screens){@Override public void consume(BufferedImage screen) {
			_screenObserved = screen;
			System.out.println(screen);
		}};
		
		_subject.startShowingMyScreen();
		while (_screenObserved == null) Threads.sleepWithoutInterruptions(10);
		while (!Images.isSameImage(image1, _screenObserved)) Threads.sleepWithoutInterruptions(10);

		_clock.advanceTime(500);
		while (!Images.isSameImage(image2, _screenObserved)) Threads.sleepWithoutInterruptions(10);

		_clock.advanceTime(500);
		while (!Images.isSameImage(image3, _screenObserved)) Threads.sleepWithoutInterruptions(10);
		
		_context.assertIsSatisfied();
		
	}

	private PublicKey ownKey() {
		return _keys.ownPublicKey();
	}

	private BufferedImage loadImage(String fileName) throws Hiccup {
		return _imageFactory.createBufferedImage(getImage(getClass().getResource(fileName)));
	}

}
