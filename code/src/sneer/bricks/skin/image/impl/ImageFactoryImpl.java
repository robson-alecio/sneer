package sneer.bricks.skin.image.impl;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import sneer.bricks.hardware.cpu.exceptions.Hiccup;
import sneer.bricks.hardware.gui.images.Images;
import sneer.bricks.skin.image.ImageFactory;
import static sneer.foundation.environments.Environments.my;

class ImageFactoryImpl implements ImageFactory {
	
	protected HashMap<String,ImageIcon> map = new HashMap<String,ImageIcon>();
	protected HashMap<ImageIcon,Image> mapBytes = new HashMap<ImageIcon,Image>();
	
    @Override
	public ImageIcon getIcon(String relativeImagePath){
		return getIcon(ImageFactoryImpl.class, relativeImagePath);
	}
	
    @Override
	public ImageIcon getIcon(File file){
		try {
			return getIcon(file.toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
    
    @Override
    public ImageIcon getIcon(Class<?> anchor, String relativeImagePath){
    	return getIcon(anchor.getResource(relativeImagePath));
    }
 
    /** @throws Hiccup */
    @Override
    public BufferedImage createBufferedImage(Image image) throws Hiccup {
    	if (image instanceof BufferedImage)
			return (BufferedImage)image;
    	
        try {
			return tryToCreateBufferedImage(image);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
    }

	private ImageIcon getIcon(URL url){
		String id = new StringBuffer().append("|").append(url.getPath()).toString(); 
		if(map.containsKey(id)){
			return map.get(id);
		}
		Image img = my(Images.class).getImage(url);
		ImageIcon icon = new ImageIcon(img);
			
		map.put(id, icon);
		mapBytes.put(icon, img);
		return map.get(id);		
	}
	
	private BufferedImage tryToCreateBufferedImage(Image image)	throws InterruptedException {
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

    @Override
    public GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }
     
    @Override
    public BufferedImage toCompatibleImage(BufferedImage image, GraphicsConfiguration gc) {
        if (gc == null)
            gc = getDefaultConfiguration();
        int w = image.getWidth();
        int h = image.getHeight();
        int transparency = image.getColorModel().getTransparency();
        BufferedImage result = gc.createCompatibleImage(w, h, transparency);
        Graphics2D g2 = result.createGraphics();
        g2.drawRenderedImage(image, null);
        g2.dispose();
        return result;
    }
        
    @Override
    public BufferedImage copy(BufferedImage source, BufferedImage target) {
        Graphics2D g2 = target.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        double scalex = (double) target.getWidth()/ source.getWidth();
        double scaley = (double) target.getHeight()/ source.getHeight();
        AffineTransform xform = AffineTransform.getScaleInstance(scalex, scaley);
        g2.drawRenderedImage(source, xform);
        g2.dispose();
        return target;
    }
     
    @Override
    public BufferedImage getScaledInstance(Image image, int width, int height) throws Hiccup {
    	BufferedImage converted = createBufferedImage(image);
    	return getScaledInstance(converted, width, height, null);
    }
    
    @Override
    public BufferedImage getScaledInstance(Image image, double scale) throws Hiccup {
    	BufferedImage converted = createBufferedImage(image);
    	return getScaledInstance(converted, (int)(converted.getWidth()*scale), (int)(converted.getHeight()*scale));
    }
    
    @Override
    public BufferedImage getScaledInstance(Image image, int width, int height, GraphicsConfiguration gc) throws Hiccup {
      	BufferedImage converted = createBufferedImage(image);
      	
      	if (gc == null)
            gc = getDefaultConfiguration();
        int transparency = converted.getColorModel().getTransparency();
        return copy(converted, gc.createCompatibleImage(width, height, transparency));
    }

    @Override
	public URL getImageUrl(String name) {
		return this.getClass().getResource(name);
	}

    @Override
	public Image fromPngData(byte[] bytes) {
    	try {
			return ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

    /** @throws Hiccup */
    @Override
	public byte[] toPngData(BufferedImage img) throws Hiccup {
    	ByteArrayOutputStream result = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "png", result);
		} catch (IOException e1) {
			throw new IllegalStateException();
		}
		return result.toByteArray();
	}
    
	private void loadImage(Image image) throws InterruptedException, IllegalArgumentException {
        Component dummy = new Component(){ private static final long serialVersionUID = 1L; };
        MediaTracker tracker = new MediaTracker(dummy);
        tracker.addImage(image, 0);
        tracker.waitForID(0);
        if (tracker.isErrorID(0))
            throw new IllegalArgumentException();
    }
 
    private ColorModel getColorModel(Image image) throws InterruptedException, IllegalArgumentException {
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        if (!pg.grabPixels())
            throw new IllegalArgumentException();
        return pg.getColorModel();
    }
}