/*
 *                               [ JIGA ]
 *
 * Copyright (c) 2003 Glenn Sanson <glenn.sanson at free.fr>
 *
 * This code is distributed under the GNU Library General Public License 
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version. 
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc., 
 * 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *               
 *                 [http://glenn.sanson.free.fr/jiga/]
 */

package frozenbubble.game.net.library.jiga;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;

/**
 * An extention of <code>java.awt.Image</code> that allow features implemented in java 1.2+ (AffineTransform).
 * This class mainly include image rotation for the moment.
 * It can also be useful to retrieve pixels data associated with an image
 * @author Glenn Sanson
 */
public class EffectImage {

    public final static int RENDERING_FAST = 0;
    public final static int RENDERING_SMOOTH = 1;
        
    GameApplet applet;

    int[] imageData;
    int imgWidth, imgHeight;
    
    @SuppressWarnings("unused")
	private EffectImage() {}
    
    /**
     * Defines an Image with no Alpha
     * @param applet1 The <code>GameApplet</code> of the current process
     * @param sourceImage The input image
     */
    public EffectImage(GameApplet applet1, String sourceImage) {
        this(applet1, sourceImage, null);
    }

    /**
     * Defines an image with alpha component. 
     * This constructor was defined to allow use of JPEG images as source for non-alpha data.
     * The images used for the alpha component must have the same size as the normal image  
     * @param applet1 The <code>GameApplet</code> of the current process
     * @param sourceImage The input image
     * @param alphaImage An image used to retrieve alpha component (Red componant is used). 
     */
    public EffectImage(GameApplet applet1, String sourceImage, String alphaImage) {

        this.applet = applet1;
        
        Image img = applet1.getGameMedia().loadImage(sourceImage);
        
        imgWidth = img.getWidth(applet1);
        imgHeight = img.getHeight(applet1);
        
        imageData = new int[imgWidth * imgHeight];
        
        PixelGrabber pg = new PixelGrabber(img, 0, 0, imgWidth, imgHeight, imageData, 0, imgWidth);
        try {
            pg.grabPixels();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        if (alphaImage != null) {
        
            Image alpha = applet1.getGameMedia().loadImage(alphaImage);
            int[] alphaData = new int[imgWidth * imgHeight];
            
            // Alpha is supposed to have same width & height        
            pg = new PixelGrabber(alpha, 0, 0, imgWidth, imgHeight, alphaData, 0, imgWidth);
            try {
                pg.grabPixels();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            
            for (int i=0 ; i<imageData.length ; i++) {
                imageData[i] = (imageData[i] & 0xFFFFFF) | ((alphaData[i] & 0xFF0000) << 8);
            }
        }
    }
    
    /**
     * Calculates the output of a rotation of the original image
     * @param theta The rotation angle (RAD)
     * @param x The X coordinate of the center
     * @param y The Y coordinate of the center
     * @return an <code>Image</code> of the rotation
     */
    public Image getRotate(double theta, double x, double y) {
        return getFastRotate(theta, x, y);
    }
    
    /**
     * Calculates the output of a rotation of the original image
     * @param theta The rotation angle (RAD)
     * @param x The X coordinate of the center
     * @param y The Y coordinate of the center
     * @param rendering The rendering method to use
     * @return an <code>Image</code> of the rotation
     */
    public Image getRotate(double theta, double x, double y, @SuppressWarnings("unused")
	int rendering) {
        /* 
        if (rendering == EffectImage.RENDERING_SMOOTH) {
        }
        */
        
        return getFastRotate(theta, x, y);
    }
    
    /**
     * Calculates the output of a rotation of the original image, using a fast algorithme.
     * This algorithm does not interpolate pixel values
     * @param theta The rotation angle (RAD)
     * @param x The X coordinate of the center
     * @param y The Y coordinate of the center
     * @return an <code>Image</code> of the rotation
     */    
    private Image getFastRotate(double theta, double x, double y) {

        int[] output = new int[imageData.length];
                
        // Reusable values
        double cosTheta = Math.cos(-theta);
        double sinTheta = Math.sin(-theta);
        
        double deltaX = x * (1. - cosTheta) + y * sinTheta;
        double deltaY = y * (1. - sinTheta) - y * cosTheta;
        
        // Calculate
        for (int j=0 ; j<imgHeight ; j++) {
            for (int i=0 ; i<imgWidth ; i++) {
                // For each output point
                // Find its origin
                int orX = (int)Math.round(i * cosTheta - j * sinTheta + deltaX);
                int orY = (int)Math.round(i * sinTheta + j * cosTheta + deltaY);
                                
                if (orX < 0 || orX >= imgWidth || orY < 0 || orY >= imgHeight) {
                    // out of scope
                    output[i + j * imgWidth] = 0;
                }
                else {
                    // Compute value
                    output[i + j * imgWidth] = imageData[orX + orY * imgWidth];
                }
            }
        }
        
        Image image = applet.createImage(new MemoryImageSource(imgWidth, imgHeight, output, 0, imgWidth));
        
        MediaTracker mediaTracker = new MediaTracker(applet);
        
		mediaTracker.addImage(image, 0);
		try {
			mediaTracker.waitForID(0);
		}
		catch(Exception e) {
			System.err.println("Rotation error");
		}
        
        return image;
    }
    
    /**
     * Retrieve pixels 
     * @return a 1D representation of pixels data
     */
    public int[] getData() {
    	return imageData;
    }
}
