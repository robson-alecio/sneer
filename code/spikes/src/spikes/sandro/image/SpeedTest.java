package spikes.sandro.image;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import sneer.bricks.hardware.gui.images.Images;
import static sneer.foundation.environments.Environments.my;


public class SpeedTest {
	
	public static void main(String[] args) throws Exception {
		
		int[] pixels01;
		{
			BufferedImage img =createBufferedImage(my(Images.class).getImage(SpeedTest.class.getResource("screen1.png")));
			long t0 = System.nanoTime();
			pixels01 = toPixel01(img);
			long t1 = System.nanoTime();
			System.out.println("toPixels01(): \n" + (t1 - t0) + "\n");
		}
		
		int[] pixels02;
		{
			BufferedImage img =createBufferedImage(my(Images.class).getImage(SpeedTest.class.getResource("screen1.png")));
			long t0 = System.nanoTime();
			pixels02 = toPixels02(img);
			long t1 = System.nanoTime();
			System.out.println("toPixels02(): \n" + (t1 - t0) + "\n");
		}

		{
			long t0 = System.nanoTime();
			boolean isEqual = Arrays.equals(pixels01, pixels02);
			long t1 = System.nanoTime();
			System.out.println("Arrays.equals(" + isEqual + "): \n" + (t1 - t0) + "\n");
		}
		
		{
			long t0 = System.nanoTime();
			for (int i = 0; i < pixels01.length; i++) {
				if(pixels01[i]!=pixels02[i]) throw new RuntimeException("ERROR!");
			}
			long t1 = System.nanoTime();
			System.out.println("for: \n" + (t1 - t0));
		}
		
		{
		 	testImageIoWrite("png");
		 	testImageIoWrite("jpeg");
		 	testImageIoWrite("gif");
		 	testImageIoWrite("bmp");
		 	testImageIoWrite("wbmp");
		}
	}

	private static void testImageIoWrite(String type) throws InterruptedException, IOException {
		BufferedImage img01 =createBufferedImage(my(Images.class).getImage(SpeedTest.class.getResource("screen1.png")));

		long t0 = System.nanoTime();
		byte[] byteArray = convert(type, img01);
		long t1 = System.nanoTime();
		System.out.println("Conversion: " + (t1 - t0) + "\n");

		
		
		if(byteArray.length==0){
			System.out.println("\nnot converted to " + type + " (" + byteArray.length + " bytes)");
			return;
		}

		System.out.println("\nconverted to " + type + " (" + byteArray.length + " bytes)");

		BufferedImage img02 =ImageIO.read(new ByteArrayInputStream(byteArray));
		boolean isEqual = Arrays.equals(toPixel01(img01), toPixel01(img02));
		System.out.println("[" + type + "] ImageIO.write() = " + isEqual);
	}

	private static byte[] convert(String type, BufferedImage img01)
			throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		ImageIO.write(img01, type, result);
		byte[] byteArray = result.toByteArray();
		result.close();
		return byteArray;
	}
	
	public static int[] toPixel01(BufferedImage image) {
		PixelGrabber result = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), true);
		try {
			result.grabPixels();
		} catch (InterruptedException e) {
			throw new IllegalStateException();
		}

		return (int[])result.getPixels();
	}
	
	public static int[] toPixels02(BufferedImage image) {
        return image.getRGB(0,0,image.getWidth(), image.getHeight(),null,0,image.getWidth());
	}
	
	private static BufferedImage createBufferedImage(Image image)	throws InterruptedException {
		loadImage(image);
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        ColorModel cm = getColorModel(image);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage bi = gc.createCompatibleImage(w, h, cm.getTransparency());
        Graphics2D g = bi.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bi;
	}
	
    private static ColorModel getColorModel(Image image) throws InterruptedException, IllegalArgumentException {
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        if (!pg.grabPixels())
            throw new IllegalArgumentException();
        return pg.getColorModel();
    }
	private static void loadImage(Image image) throws InterruptedException, IllegalArgumentException {
        Component dummy = new Component(){ private static final long serialVersionUID = 1L; };
        MediaTracker tracker = new MediaTracker(dummy);
        tracker.addImage(image, 0);
        tracker.waitForID(0);
        if (tracker.isErrorID(0))
            throw new IllegalArgumentException();
    }
}