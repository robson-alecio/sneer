package snapps.watchme.codec.tests;

import java.awt.image.BufferedImage;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import static wheel.io.ui.graphics.Images.copy;

public class ImageCodecTest extends TestThatIsInjected {
	
	@Inject
	static private ImageCodec _subject;
	
	@Test
	public void encodeSameImage() {
		final BufferedImage image1 = new BufferedImage(64, 64, BufferedImage.TYPE_3BYTE_BGR);
	
		Iterable<ImageDelta> deltas = _subject.encodeDeltas(image1, copy(image1));
		assertFalse(deltas.iterator().hasNext());
	}

	@Test
	@Ignore
	public void encodeImage() {
		
		final BufferedImage image1 = new BufferedImage(128, 64, BufferedImage.TYPE_3BYTE_BGR);
		final BufferedImage image2 = image1.getSubimage(0, 0, 64, 64);
		final BufferedImage delta   = image1.getSubimage(64, 0, 64, 64);

		Iterable<ImageDelta> deltas = _subject.encodeDeltas(image1, image2);
		Assert.assertEquals(delta, deltas.iterator().next());
	}
}
