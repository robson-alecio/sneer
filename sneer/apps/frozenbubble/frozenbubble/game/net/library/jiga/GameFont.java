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

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Point;

/**
 * This class maps a bitmap to a game font. 
 * This font has -by definition- no dependency to the user system.
 * The font is defined as an <code>Image</code> representing all the characters put on a line
 * @author Glenn Sanson
 * @see TextSprite
 * @see ScrollingTextSprite
 */
public class GameFont {
    /** Defines the number of pixels between 2 characters */
    public int SEPARATOR_WIDTH = 1;
    /** Defines the size in pixels of a whitespace */
    public int SPACE_CHAR_WIDTH = 6;

    private Image fontMap;

    private char[] characters;

    private int[] position;

    private GameApplet applet;

    /**
     * Definition of a font with its character set.
     * Each character is defined by by its position in the corresponding <code>Image</code>.
     * The first character takes the height of the picture, its width is defined as [0;{end_pixel_0}], 
     * the width of the second character is defined as [{end_pixel_0+1};{end_pixel_1}], and so on...
     * @param fontMap1 the Image representing all the characters of the set
     * @param characters1 The set of <code>char</code> represented by the font
     * @param position1 The X position (pixel) of the end of the character 
     * @param applet1 The <code>GameApplet</code> of the current process
     */
    public GameFont(Image fontMap1, char[] characters1, int[] position1, GameApplet applet1) {
        this.fontMap = fontMap1;
        this.characters = characters1;
        this.position = position1;
        this.applet = applet1;
    }

    /**
     * Definition on an empty character set. 
     * The font is definable afterward using the <code>setCharMap</code> function
     * @param fontMap1 the Image representing all the characters of the set
     * @param applet1 The <code>GameApplet</code> of the current process
     */
    public GameFont(Image fontMap1, GameApplet applet1) {
        this(fontMap1, new char[0], new int[0], applet1);
    }

    /**
     * Defines/Modifies the definition of the characters set defined by the font.
     * @param characters1 The set of <code>char</code> represented by the font
     * @param position1 The X position (pixel) of the end of the character (See details in constructor)
     */
    public void setCharMap(char[] characters1, int[] position1) {
        this.characters = characters1;
        this.position = position1;
    }

    /**
     * Gets width of a character in the current graphical font
     * @param c The character to test
     * @return The width of the charater (0 if unknown)
     */
    public final int charSize(char c) {
        if (c == ' ') {
            return SPACE_CHAR_WIDTH;
        }

        int index = getCharIndex(c);

        if (index == -1) {
            return 0;
        }

        if (index == position.length - 1) {
            return fontMap.getWidth(applet) - position[index];
        }

        return position[index + 1] - position[index];
    }

    /**
     * Draws a character. If the character is not known, this function does nothing
     * @param c The character to draw
     * @param g The <code>Graphics</code> used to paint the character
     * @param p The upper-left corner of the character
     * @return The width of the graphical representation of the character 
     */
    public final int paintChar(char c, Graphics g, Point p) {
        if (c == ' ') {
            return SPACE_CHAR_WIDTH + SEPARATOR_WIDTH;
        }

        int index = getCharIndex(c);

        if (index == -1) {
            return 0;
        }

        int imageWidth = 0;

        if (index == position.length - 1) {
            imageWidth = fontMap.getWidth(applet) - position[index];
        } else {
            imageWidth = position[index + 1] - position[index];
        }

        g.clipRect(p.x, p.y, imageWidth, fontMap.getHeight(applet));
        g.drawImage(fontMap, p.x - position[index], p.y, applet);

        return imageWidth + SEPARATOR_WIDTH;
    }

    private final int getCharIndex(char c) {
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] == c) {
                return i;
            }
        }

        return -1;
    }
}