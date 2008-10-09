package snapps.watchme.codec;

import java.awt.image.BufferedImage;
import java.util.List;

import sneer.kernel.container.Brick;
import wheel.lang.exceptions.Hiccup;

public interface ImageCodec extends Brick {

	List<ImageDelta> encodeDeltas(BufferedImage original, BufferedImage target) throws Hiccup;

	BufferedImage decodeDeltas(BufferedImage original, Iterable<ImageDelta> deltas);

}
