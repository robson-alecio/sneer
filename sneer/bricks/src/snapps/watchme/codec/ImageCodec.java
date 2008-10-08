package snapps.watchme.codec;

import java.awt.image.BufferedImage;
import java.util.List;

import sneer.kernel.container.Brick;

public interface ImageCodec extends Brick {

	List<ImageDelta> encodeDeltas(BufferedImage original, BufferedImage target);

	BufferedImage decodeDeltas(BufferedImage original, Iterable<ImageDelta> deltas);

}
