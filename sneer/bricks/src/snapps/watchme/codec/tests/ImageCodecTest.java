package snapps.watchme.codec.tests;

import java.awt.image.BufferedImage;

import org.junit.Assert;
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
		final BufferedImage image1 = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
	
		Iterable<ImageDelta> deltas = _subject.encodeDeltas(image1, copy(image1));
		assertFalse(deltas.iterator().hasNext());
	}

	@Test
	public void encodeImage() {
		
		final BufferedImage image1 = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
		final BufferedImage image2 = new BufferedImage(2, 2, BufferedImage.TYPE_3BYTE_BGR);

		Iterable<ImageDelta> deltas = _subject.encodeDeltas(image1, image2);
		BufferedImage result = _subject.decodeDeltas(image1, deltas);
		Assert.assertEquals(image2, result);
	}

}
