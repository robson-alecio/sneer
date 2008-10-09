package snapps.watchme.codec.tests;

import static wheel.io.ui.graphics.Images.copy;
import static wheel.io.ui.graphics.Images.getImage;

import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.skin.image.ImageFactory;
import wheel.io.serialization.impl.XStreamBinarySerializer;
import wheel.io.ui.graphics.Images;
import wheel.lang.exceptions.Hiccup;

public class ImageCodecTest extends TestThatIsInjected {
	
	@Inject
	static private ImageCodec _subject;
	
	@Inject
	private static ImageFactory _imageFactory;
	
	@Test
	public void encodeSameImage() throws Hiccup {
		final BufferedImage image1 = new BufferedImage(64, 64, BufferedImage.TYPE_3BYTE_BGR);
	
		Iterable<ImageDelta> deltas = _subject.encodeDeltas(image1, copy(image1));
		assertFalse(deltas.iterator().hasNext());
	}

	@Test
	public void encodeImage() throws Exception, Hiccup {
		encodeAndDecode("together1.png", "together3.png");
	}
	
	@Test
	public void encodeChangeInBorder() throws Exception, Hiccup {
		encodeAndDecode("together1.png", "together2.png");
	}

	@Test
	public void encodeChangeInBorderNotMultipleOf64() throws Exception, Hiccup {
		encodeAndDecode("togetherBorder1.png", "togetherBorder2.png");
	}

	private void encodeAndDecode(String imgAName, String imgBName)	throws Hiccup {
		final BufferedImage imageA = loadImage(imgAName);
		final BufferedImage imageB = loadImage(imgBName);
		
		int[] dataA =_imageFactory.toSerializableData(imageA);
		
		ImageDelta full = new ImageDelta(dataA,0,0, imageA.getWidth(), imageA.getHeight());
		int fullSizeBytes = serialize(full).length;
		
		List<ImageDelta> deltas = _subject.encodeDeltas(imageA, imageB);
		int deltaSizeBytes = serialize(deltas.toArray()).length;
		
		assertTrue(deltas.size()>0);
		assertTrue(deltaSizeBytes<fullSizeBytes);
		
		System.out.println(deltas.size());
		System.out.println(fullSizeBytes);
		System.out.println(deltaSizeBytes);

		BufferedImage result = _subject.decodeDeltas(imageA, deltas);

		deltas.clear();
		int emptyDeltaSizeBytes = SerializationUtils.serialize(deltas.toArray()).length;
		assertTrue(deltaSizeBytes>emptyDeltaSizeBytes);
		
		Assert.assertTrue(Images.isSameImage(imageB, result));
	}

	private byte[] serialize(Object obj) {
		return new XStreamBinarySerializer().serialize(obj);
	}

	private BufferedImage loadImage(String fileName) throws Hiccup {
		return _imageFactory.createBufferedImage(getImage(getClass().getResource(fileName)));
	}
}
