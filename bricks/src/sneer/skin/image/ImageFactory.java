package sneer.skin.image;

import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;

public interface ImageFactory extends DefaultIcons{

	ImageIcon getIcon(String relativeImagePath);

	ImageIcon getIcon(String relativeImagePath,	boolean hasShadow);

	ImageIcon getIcon(Class<?> anchor,	String relativeImagePath, boolean hasShadow);

	ImageIcon getIcon(File file, boolean hasShadow);

	BufferedImage createBufferedImage(Image image) throws InterruptedException, IllegalArgumentException;

	GraphicsConfiguration getDefaultConfiguration();

	BufferedImage toCompatibleImage(BufferedImage image, GraphicsConfiguration gc);

	BufferedImage copy(BufferedImage source, BufferedImage target);

	BufferedImage getScaledInstance(BufferedImage image, double scale);

	BufferedImage getScaledInstance(BufferedImage image, int width, int height);

	BufferedImage getScaledInstance(BufferedImage image, int width, int height, GraphicsConfiguration gc);

}