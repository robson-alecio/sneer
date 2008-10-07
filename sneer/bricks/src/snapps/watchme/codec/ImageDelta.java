package snapps.watchme.codec;

import java.awt.image.BufferedImage;

public class ImageDelta {

	public static final int FULL_SIZE = -1;
	public final BufferedImage _targetImage;
	public final int _x;
	public final int _y;

	public ImageDelta(BufferedImage targetImage) {
		this(targetImage, FULL_SIZE, FULL_SIZE);
	}
	
	public ImageDelta(BufferedImage targetImage, int x, int y) {
		_targetImage = targetImage;
		_x = x;
		_y = y;
	}
}
