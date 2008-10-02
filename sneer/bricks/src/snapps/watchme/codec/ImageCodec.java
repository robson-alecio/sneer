package snapps.watchme.codec;

import java.awt.image.BufferedImage;

import sneer.kernel.container.Brick;

public interface ImageCodec extends Brick {

	Iterable<ImageDelta> encodeDeltas(BufferedImage imageA, BufferedImage imageB);

	BufferedImage decodeDeltas(BufferedImage imageA, Iterable<ImageDelta> deltas);

}
