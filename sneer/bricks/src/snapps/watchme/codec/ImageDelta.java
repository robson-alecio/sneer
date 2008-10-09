package snapps.watchme.codec;

import java.io.Serializable;

public class ImageDelta implements Serializable {

	public final int x;
	public final int y;
	
	public final int width;
	public final int height;
	
	public int[]  imageData;
	
	public ImageDelta(int[] pImageData, int pX, int pY, int pWidth, int pHeight) { 
		this.x = pX;
		this.y = pY;
		this.width = pWidth;
		this.height = pHeight;
		this.imageData =  pImageData;
	}
}