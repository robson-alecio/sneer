package snapps.watchme.codec;

import java.awt.image.BufferedImage;
import java.util.List;

import sneer.kernel.container.Brick;
import wheel.lang.exceptions.Hiccup;

public interface ImageCodec extends Brick {

	public interface Decoder {
		boolean applyDelta(ImageDelta delta);

		BufferedImage screen();
	}
	
	public interface Encoder {
		public List<ImageDelta> generateDeltas(BufferedImage shot) throws Hiccup;
	}	

	Encoder createEncoder() throws Hiccup;	
	
	Decoder createDecoder();
	
	Decoder createDecoder(BufferedImage image);

}
