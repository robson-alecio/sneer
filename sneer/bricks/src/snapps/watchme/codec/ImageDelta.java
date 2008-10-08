package snapps.watchme.codec;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.Serializable;

public class ImageDelta implements Serializable {

	public final int x;
	public final int y;
	
	public final int width;
	public final int height;
	
	private transient Image cachedImage;
	private int[] serializableData;

	public ImageDelta(BufferedImage pImageCell, int pX, int pY) throws InterruptedException { //Refactor rename p* -> *
		this(pImageCell, pX, pY, pImageCell.getWidth(), pImageCell.getHeight());
	}
	
	public ImageDelta(Image pImageCell, int pX, int pY, int pWidth, int pHeight) throws InterruptedException { 
		this.x = pX;
		this.y = pY;
		this.width = pWidth;
		this.height = pHeight;
		this.cachedImage = pImageCell;
		this.serializableData = toSerializableData(this.cachedImage);
	}
	
	public int[] data() {
		return serializableData;
	}
	
	public Image imageCell() {
		if(cachedImage==null)
			cachedImage = loadImage();
		return cachedImage;
	}
	 
	private int[] toSerializableData(Image img) throws InterruptedException {
		int[] pixels = new int[width * height];
		PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, pixels,	0, width);
		pg.grabPixels();
		return pixels;
	}  
	 
	private Image loadImage() {
		MemoryImageSource mis = new MemoryImageSource(width, height, serializableData,	0, width);
		Toolkit tk = Toolkit.getDefaultToolkit();
		return tk.createImage(mis);
	} 
}