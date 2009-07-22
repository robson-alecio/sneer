package spikes.sneer.bricks.snapps.watchme.codec;

import java.awt.image.BufferedImage;
import java.util.List;

import sneer.bricks.hardware.cpu.exceptions.Hiccup;
import sneer.foundation.brickness.Brick;

@Brick
public interface ImageCodec {

	public interface Decoder {
		boolean applyDelta(ImageDelta delta);

		BufferedImage screen();
	}
	
	public interface Encoder {
		public List<ImageDelta> generateDeltas(BufferedImage shot) throws Hiccup;
	}	

	Encoder createEncoder();	
	
	Decoder createDecoder();
	
	Decoder createDecoder(BufferedImage image);

}
