package snapps.watchme.codec.tests;

import static wheel.io.ui.graphics.Images.copy;
import static wheel.io.ui.graphics.Images.getImage;

import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.skin.image.ImageFactory;

public class ImageCodecTest extends TestThatIsInjected {
	
	@Inject
	static private ImageCodec _subject;
	
	@Inject
	private static ImageFactory _imageFactory;
	
	@Test
	public void encodeSameImage() {
		final BufferedImage image1 = new BufferedImage(64, 64, BufferedImage.TYPE_3BYTE_BGR);
	
		Iterable<ImageDelta> deltas = _subject.encodeDeltas(image1, copy(image1));
		assertFalse(deltas.iterator().hasNext());
	}

	@Test
	@Ignore
	public void encodeImage() throws Exception {
		
		final BufferedImage image1 = loadImage("together1.png");
		final BufferedImage image2 = loadImage("together2.png");
		
		ImageDelta full = new ImageDelta(image1,0,0);
		int fullSizeBytes = SerializationUtils.serialize(full).length;
		
		List<ImageDelta> deltas = _subject.encodeDeltas(image1, image2);
		int deltaSizeBytes = SerializationUtils.serialize(deltas.toArray()).length;
		
		assertTrue(deltas.size()>0);
		assertTrue(deltaSizeBytes>0);
		assertTrue(deltaSizeBytes<=fullSizeBytes);
		
//		System.out.println(deltas.size());
//		System.out.println(fullSizeBytes);
//		System.out.println(deltaSizeBytes);
		
		BufferedImage result = _subject.decodeDeltas(image1, deltas);
		Assert.assertEquals(image2, result);
	}

	private BufferedImage loadImage(String fileName) throws Exception {
		return _imageFactory.createBufferedImage(getImage(getClass().getResource(fileName)));
	}
}
