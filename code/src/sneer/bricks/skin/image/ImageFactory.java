package sneer.bricks.skin.image;

import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;

import sneer.bricks.hardware.cpu.exceptions.Hiccup;
import sneer.foundation.brickness.Brick;

@Brick
public interface ImageFactory extends DefaultIcons{

	ImageIcon getIcon(File file);
	ImageIcon getIcon(String relativeImagePath);
	ImageIcon getIcon(Class<?> anchor,	String relativeImagePath);

	BufferedImage createBufferedImage(Image image) throws Hiccup;
	BufferedImage toCompatibleImage(BufferedImage image, GraphicsConfiguration gc);
	BufferedImage copy(BufferedImage source, BufferedImage target);

	GraphicsConfiguration getDefaultConfiguration();

	BufferedImage getScaledInstance(Image image, double scale) throws Hiccup;
	BufferedImage getScaledInstance(Image image, int width, int height) throws Hiccup;
	BufferedImage getScaledInstance(Image image, int width, int height, GraphicsConfiguration gc) throws Hiccup;
	
	Image fromPngData(byte[] data);
	byte[] toPngData(BufferedImage img) throws Hiccup;

}