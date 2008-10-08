package snapps.watchme.codec;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.Serializable;

public class ImageDelta implements Serializable {

	public final int x;
	public final int y;
	
	private transient BufferedImage cachedImage;
	private int[] serializableData;

	public ImageDelta(BufferedImage pImageCell, int pX, int pY) { //Refactor rename p* -> *
		this.cachedImage = pImageCell;
		this.serializableData = toByteArray(this.cachedImage);
		this.x = pX;
		this.y = pY;
	}
	
	public int[] data() {
		return serializableData;
	}
	
	public BufferedImage imageCell() {
//		if(cachedImage==null)
//			cachedImage = toBufferedImage(serializableData);
		return cachedImage;
	}

//	private BufferedImage toBufferedImage(int[] data) {
//		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
//	}
	
	private int[] toByteArray(BufferedImage image){
		WritableRaster raster = image.getRaster();
	    DataBufferInt buffer = (DataBufferInt)raster.getDataBuffer();
	    return buffer.getData();
	}	
}
