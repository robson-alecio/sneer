package sneer.skin.image.impl;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

import sneer.skin.image.ImageFactory;

import com.jhlabs.image.ShadowFilter;

public class ImageFactoryImpl implements ImageFactory {
	
	protected HashMap<String,ImageIcon> map = new HashMap<String,ImageIcon>();
	protected HashMap<ImageIcon,Image> mapBytes = new HashMap<ImageIcon,Image>();
	
    @Override
	public ImageIcon getIcon(String relativeImagePath){
		return getIcon(ImageFactoryImpl.class, relativeImagePath, false);
	}

    @Override
	public ImageIcon getIcon(String relativeImagePath, boolean hasShadow){
		return getIcon(ImageFactoryImpl.class, relativeImagePath, hasShadow);
	}	
	
    @Override
	public ImageIcon getIcon(File file, boolean hasShadow){
		try {
			return getIcon(file.toURI().toURL(), hasShadow);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
    
    @Override
    public ImageIcon getIcon(Class<?> anchor, String relativeImagePath, boolean hasShadow){
    	return getIcon(anchor.getResource(relativeImagePath), hasShadow);
    }

	public ImageIcon getIcon(URL url, boolean hasShadow){
		String id = new StringBuffer().append(hasShadow).append("|").append(url.getPath()).toString(); 
		if(map.containsKey(id)){
			return map.get(id);
		}
		Image img = Toolkit.getDefaultToolkit().getImage(url);
		
		InputStream in;
		try {
			in = url.openStream();
		} catch (IOException e) {
			return new ImageIcon(img);
		}
		ImageIcon icon = createImageIcon(hasShadow, img, in);
			
		map.put(id, icon);
		mapBytes.put(icon, img);
		return map.get(id);		
	}
 
    @Override
    public BufferedImage createBufferedImage(Image image) throws InterruptedException, IllegalArgumentException {
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
    public BufferedImage getScaledInstance(BufferedImage image, int width, int height) {
    	return getScaledInstance(image, width, height, null);
    }
    
    @Override
    public BufferedImage getScaledInstance(BufferedImage image, double scale) {
    	return getScaledInstance(image, (int)(image.getWidth()*scale), (int)(image.getHeight()*scale));
    }
    
    @Override
    public BufferedImage getScaledInstance(BufferedImage image, int width, int height, GraphicsConfiguration gc) {
        if (gc == null)
            gc = getDefaultConfiguration();
        int transparency = image.getColorModel().getTransparency();
        return copy(image, gc.createCompatibleImage(width, height, transparency));
    }

    @Override
	public URL getImageUrl(String name) {
		return this.getClass().getResource(name);
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

	private ImageIcon createImageIcon(boolean hasShadow, Image img, InputStream in) {
		BufferedImage buf;
		ImageIcon icon;
		try{
			buf = createBufferedImage(img);
			if(hasShadow){
				ShadowFilter f = new ShadowFilter();
				f.setOpacity(.7f);
				buf = f.filter(buf, null);
			}
			
			icon = new ImageIcon(buf);
		} catch (Exception e) {
			icon = new ImageIcon(img);
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				//ignore
			}
		}
		return icon;
	}
}