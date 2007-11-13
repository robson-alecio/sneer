/*
 * [ JIGA ]
 * 
 * Copyright (c) 2003 Glenn Sanson <glenn.sanson at free.fr>
 * 
 * This code is distributed under the GNU Library General Public License
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * 
 * [http://glenn.sanson.free.fr/jiga/]
 */

package frozenbubble.game.net.library.jiga;

import java.io.InputStream;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.applet.AudioClip;

/**
 * A loader for images, sounds as well as raw data.
 * For performance reasons, each medium is stored in memory.
 * To avoid any in-game slowdown due to the media loading process,
 * everything can be preloaded at initialisation of the applet or screen 
 * @author Glenn Sanson
 */
public class GameMedia {
    private MediaTracker mediaTracker;

    private GameApplet gameApplet;

    /**
     * Creates a new media loader.
     * Each <code>GameApplet</code> is associated with a <code>GameMedia</code>, which is enough for any purpose.
     * @param applet The <code>GameApplet</code> associated to this <code>GameMedia</code>
     */
    public GameMedia(GameApplet applet) {
        this.gameApplet = applet;
        this.mediaTracker = new MediaTracker(applet);
    }

    /**
     * Loads raw data
     * @param filename The name (location) of the data stream to load
     * @return A binary array representing the data file content
     * or <code>null</code> if no valid data was found at the given location
     */
    public byte[] loadData(String filename) {
        byte[] data = (byte[]) gameApplet.getGameContext().getObject(filename);

        if (data != null) {
            return data;
        }

        try {
            InputStream in = gameApplet.getClass().getResourceAsStream(filename);

            data = new byte[in.available()];

            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) in.read();
            }

            gameApplet.getGameContext().addObject(filename, data);
        } catch (Exception e) {
            System.err.println("Data File [" + filename + "] not found");
        }

        return data;
    }

    /**
     * Loads an Image. Only file formats natively recognized by Java are usable,
     * which means -at least- jpeg and gif files 
     * @param filename The name (location) of the image file to load
     * @return The image or <code>null</code> if no valid image was found at the given location
     */
    public Image loadImage(String filename) {
        Image img = (Image) gameApplet.getGameContext().getObject(filename);

        if (img != null) {
            return img;
        }

        try {
            InputStream in = gameApplet.getClass().getResourceAsStream(filename);

            byte[] buffer = new byte[in.available()];

            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte) in.read();
            }

            img = Toolkit.getDefaultToolkit().createImage(buffer);

            gameApplet.getGameContext().addObject(filename, img);

            mediaTracker.addImage(img, 0);
            mediaTracker.waitForID(0);
        } catch (Exception e) {
            System.err.println("Image File [" + filename + "] not found");
        }

        return img;
    }

    /**
     * Loads an Image and double both its width and height. 
     * Very useful for remakers (most games were 320 * 200 in screen size, 
     * whereas a "readable" remake applet should be at least 640 * 480) 
     * @param filename The name (location) of the image file to load
     * @return The doubled image or <code>null</code> if no valid image was found at the given location
     */
    public Image loadDoubleImage(String filename) {
        Image img = (Image) gameApplet.getGameContext().getObject(filename);

        if (img != null) {
            return img;
        }

        try {
            InputStream in = gameApplet.getClass().getResourceAsStream(filename);

            byte[] buffer = new byte[in.available()];

            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte) in.read();
            }

            img = Toolkit.getDefaultToolkit().createImage(buffer);

            mediaTracker.addImage(img, 0);
            mediaTracker.waitForID(0);

            img = img.getScaledInstance(img.getWidth(gameApplet) * 2, img.getHeight(gameApplet) * 2, Image.SCALE_DEFAULT);

            gameApplet.getGameContext().addObject(filename, img);

            mediaTracker.addImage(img, 0);
            mediaTracker.waitForID(0);
        } catch (Exception e) {
            System.err.println("Image File [" + filename + "] not found");
        }

        return img;
    }

    /**
     * Loads an Image and change its size. Image can be smaller or larger than the original.
     * It needn't keep its aspect ratio.
     * @param filename The name (location) of the image file to load
     * @param width The output width of the image
     * @param height The output width of the image
     * @return The scaled image or <code>null</code> if no valid image was found at the given location
     */
    public Image loadScaledImage(String filename, int width, int height) {
        Image img = (Image) gameApplet.getGameContext().getObject(filename);

        if (img != null) {
            return img;
        }

        try {
            InputStream in = gameApplet.getClass().getResourceAsStream(filename);

            byte[] buffer = new byte[in.available()];

            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte) in.read();
            }

            img = Toolkit.getDefaultToolkit().createImage(buffer).getScaledInstance(width, height, Image.SCALE_DEFAULT);

            gameApplet.getGameContext().addObject(filename, img);

            mediaTracker.addImage(img, 0);
            mediaTracker.waitForID(0);
        } catch (Exception e) {
            System.err.println("Image File [" + filename + "] not found");
        }

        return img;
    }

    /**
     * Loads an audio clip. Java 1.1 audio support is really limited, 
     * developers should only use .au files in 8KHz/8bits/mono 
     * @param filename The name (location) of the audio clip to load
     * @return The audio clip or <code>null</code> if no valid audio data was found at the given location
     */
    public AudioClip loadAudioClip(String filename) {
        AudioClip clip = (AudioClip) gameApplet.getGameContext().getObject(filename);

        if (clip != null) {
            return clip;
        }

        clip = gameApplet.getAudioClip(gameApplet.getCodeBase(), filename);
        gameApplet.getGameContext().addObject(filename, clip);

        if (clip == null) {
            System.err.println("Audio File [" + filename + "] not found");
        }

        return clip;
    }
}