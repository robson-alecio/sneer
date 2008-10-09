package sneer.skin.image;

import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;

public interface ImageFactory extends DefaultIcons{

	ImageIcon getIcon(File file);
	ImageIcon getIcon(String relativeImagePath);
	ImageIcon getIcon(Class<?> anchor,	String relativeImagePath);

	BufferedImage createBufferedImage(Image image) throws InterruptedException, IllegalArgumentException;
	BufferedImage toCompatibleImage(BufferedImage image, GraphicsConfiguration gc);
	BufferedImage copy(BufferedImage source, BufferedImage target);

	GraphicsConfiguration getDefaultConfiguration();

	BufferedImage getScaledInstance(Image image, double scale);
	BufferedImage getScaledInstance(Image image, int width, int height);
	BufferedImage getScaledInstance(Image image, int width, int height, GraphicsConfiguration gc);
	
	Image toImage(int width, int height, int[] data);
	int[] toSerializableData(int width, int height, Image img) throws InterruptedException;

}