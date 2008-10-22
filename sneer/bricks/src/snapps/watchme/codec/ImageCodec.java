package snapps.watchme.codec;

import java.awt.image.BufferedImage;
import java.util.List;

import sneer.kernel.container.Brick;
import wheel.lang.exceptions.Hiccup;

public interface ImageCodec extends Brick {

	public interface Decoder {
		boolean applyDelta(ImageDelta delta);
	}

	List<ImageDelta> encodeDeltas(BufferedImage original, BufferedImage target) throws Hiccup;

	Decoder createDecoder(BufferedImage image);

}
