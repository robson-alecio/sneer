package snapps.watchme.codec.tests;

import java.awt.image.BufferedImage;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;

public class ImageCodecTest extends TestThatIsInjected {
	
	@Inject
	static private ImageCodec _subject;
	
	@Ignore
	@Test
	public void imageCodec() {
		
		final BufferedImage image1 = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
		final BufferedImage image2 = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);

		Iterable<ImageDelta> deltas = _subject.encodeDeltas(image1, image2);
		BufferedImage result = _subject.decodeDeltas(image1, deltas);
		Assert.assertEquals(image2, result);
	}

}
