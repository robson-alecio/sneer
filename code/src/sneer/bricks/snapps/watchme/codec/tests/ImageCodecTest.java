package sneer.bricks.snapps.watchme.codec.tests;

import static sneer.foundation.environments.Environments.my;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import sneer.bricks.hardware.cpu.exceptions.Hiccup;
import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.hardware.gui.images.Images;
import sneer.bricks.pulp.serialization.Serializer;
import sneer.bricks.skin.image.ImageFactory;
import sneer.bricks.snapps.watchme.codec.ImageCodec;
import sneer.bricks.snapps.watchme.codec.ImageDelta;
import sneer.bricks.snapps.watchme.codec.ImageCodec.Decoder;
import sneer.bricks.snapps.watchme.codec.ImageCodec.Encoder;
import sneer.foundation.brickness.testsupport.BrickTest;

public class ImageCodecTest extends BrickTest {
	
	private final ImageCodec _subject = my(ImageCodec.class);
	
	private final ImageFactory _imageFactory = my(ImageFactory.class);
	
	@Test
	public void encodeSameImage() throws Hiccup {
		final BufferedImage image1 = new BufferedImage(64, 64, BufferedImage.TYPE_3BYTE_BGR);
		final Encoder encoder = _subject.createEncoder();
		encoder.generateDeltas(image1);
		
		List<ImageDelta> deltas = encoder.generateDeltas(my(Images.class).copy(image1));
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
		
		final Encoder encoder = _subject.createEncoder();
		encoder.generateDeltas(imageA);
		
		List<ImageDelta> deltas = encoder.generateDeltas(imageB);
		int deltaSizeBytes = serialize(deltas.toArray()).length;
		
		assertTrue(deltas.size()>0);
		assertTrue(deltaSizeBytes < imageData.length);
		
		Decoder decoder = _subject.createDecoder(imageA);
		for (ImageDelta delta : deltas)
			decoder.applyDelta(delta);

		deltas.clear();
		int emptyDeltaSizeBytes = my(Lang.class).serialization().serialize(deltas.toArray()).length;
		assertTrue(deltaSizeBytes>emptyDeltaSizeBytes);
		
		Assert.assertTrue(my(Images.class).isSameImage(imageB, imageA));
	}

	private byte[] serialize(Object obj) {
		return my(Serializer.class).serialize(obj);
	}

	private BufferedImage loadImage(String fileName) throws Hiccup {
		return _imageFactory.createBufferedImage(my(Images.class).getImage(getClass().getResource(fileName)));
	}
}
