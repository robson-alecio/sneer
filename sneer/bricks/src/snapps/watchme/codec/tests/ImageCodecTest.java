package snapps.watchme.codec.tests;

import static wheel.io.ui.graphics.Images.copy;
import static wheel.io.ui.graphics.Images.getImage;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

import snapps.watchme.codec.ImageCodec;
import snapps.watchme.codec.ImageDelta;
import snapps.watchme.codec.ImageCodec.Decoder;
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
	
		List<ImageDelta> deltas = _subject.encodeDeltas(image1, copy(image1));
		assertTrue(deltas.isEmpty());
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
		
		if (GraphicsEnvironment.isHeadless())
			return;
		
		final BufferedImage imageA = loadImage(imgAName);
		final BufferedImage imageB = loadImage(imgBName);
		
		byte[] imageData =_imageFactory.toPngData(imageA);
		
		List<ImageDelta> deltas = _subject.encodeDeltas(imageA, imageB);
		int deltaSizeBytes = serialize(deltas.toArray()).length;
		
		assertTrue(deltas.size()>0);
		assertTrue(deltaSizeBytes < imageData.length);
		
		Decoder decoder = _subject.createDecoder(imageA);
		for (ImageDelta delta : deltas)
			decoder.applyDelta(delta);

		deltas.clear();
		int emptyDeltaSizeBytes = SerializationUtils.serialize(deltas.toArray()).length;
		assertTrue(deltaSizeBytes>emptyDeltaSizeBytes);
		
		Assert.assertTrue(Images.isSameImage(imageB, imageA));
	}

	private byte[] serialize(Object obj) {
		return new XStreamBinarySerializer().serialize(obj);
	}

	private BufferedImage loadImage(String fileName) throws Hiccup {
		return _imageFactory.createBufferedImage(getImage(getClass().getResource(fileName)));
	}
}
