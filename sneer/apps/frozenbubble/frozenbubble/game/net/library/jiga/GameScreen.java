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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Color;
import java.util.Vector;

/**
 * This class represents a screen in the app(let). 
 * A <code>Screen</code> should be associated with each action scheme
 * A typical usage for a game could be :
 * <ul><li>A titre/demo screen (presentation)
 * <li>A menu screen (select options, difficulty, ...)
 * <li>A game screen (or maybe several, if needed)
 * <li>An end/highscores screen</ul>
 * @author Glenn Sanson
 */
public abstract class GameScreen {
    private GameApplet currentApplet;

    private Image gameScreenImage;

    private Graphics gameScreenGraphics;

    @SuppressWarnings("unchecked")
	private Vector sprites;

    @SuppressWarnings("unchecked")
	private Vector removedSprites;

    @SuppressWarnings("unused")
	private GameScreen() {
    }

    @SuppressWarnings("unchecked")
	public GameScreen(GameApplet applet) {
        currentApplet = applet;

        gameScreenImage = applet.createImage(applet.getBufferWidth(), applet.getBufferHeight());
        gameScreenGraphics = gameScreenImage.getGraphics();

        sprites = new Vector();
        removedSprites = new Vector();
    }

    /**
     * Initialise the app(let) background
     * Default behavior 
     *
     */
    public void initBackground() {
        gameScreenGraphics.setColor(Color.white);
        gameScreenGraphics.fillRect(0, 0, currentApplet.getBufferWidth(), currentApplet.getBufferHeight());
    }

    public final GameApplet getGameApplet() {
        return currentApplet;
    }

    public final void addToBackground(Image img, Point p) {
        gameScreenGraphics.drawImage(img, p.x, p.y, currentApplet);
        currentApplet.getBackgroundGraphics().drawImage(img, p.x, p.y, currentApplet);
    }

    public final void addToBackground(Image img, Point p, Rectangle clipArea) {
        gameScreenGraphics.setClip(clipArea);
        gameScreenGraphics.drawImage(img, p.x, p.y, currentApplet);
        gameScreenGraphics.setClip(0, 0, currentApplet.getBufferWidth(), currentApplet.getBufferHeight());

        currentApplet.getBackgroundGraphics().setClip(clipArea);
        currentApplet.getBackgroundGraphics().drawImage(img, p.x, p.y, currentApplet);
        currentApplet.getBackgroundGraphics().setClip(0, 0, currentApplet.getBufferWidth(), currentApplet.getBufferHeight());
    }

    @SuppressWarnings("unchecked")
	public final void addSprite(Sprite sprite) {
        sprites.removeElement(sprite);
        sprites.addElement(sprite);
    }

    @SuppressWarnings("unchecked")
	public final void removeSprite(Sprite sprite) {
        if (sprites.removeElement(sprite)) {
            sprite.forceRefresh();
            removedSprites.addElement(sprite);
        }
    }

    @SuppressWarnings("unchecked")
	public final void spriteToBack(Sprite sprite) {
        sprites.removeElement(sprite);
        sprites.insertElementAt(sprite, 0);
    }

    @SuppressWarnings("unchecked")
	public final void spriteToFront(Sprite sprite) {
        sprites.removeElement(sprite);
        sprites.addElement(sprite);
    }

    public void setScreenPosition(int x, int y) {
        currentApplet.setScreenPosition(x, y);
    }

    public Point getScreenPosition() {
        return currentApplet.getScreenPosition();
    }

    @SuppressWarnings("unchecked")
	public final void refreshAll() {
        Graphics output = currentApplet.getBackgroundGraphics();

        for (int i = 0; i < sprites.size(); i++) {
            output.setClip(0, 0, currentApplet.getBufferWidth(), currentApplet.getBufferHeight());

            ((Sprite) sprites.elementAt(i)).refresh(gameScreenImage, output, currentApplet);
        }

        if (removedSprites.size() != 0) {
            for (int i = 0; i < removedSprites.size(); i++) {
                output.setClip(0, 0, currentApplet.getBufferWidth(), currentApplet.getBufferHeight());

                ((Sprite) removedSprites.elementAt(i)).refresh(gameScreenImage, output, currentApplet);
            }

            removedSprites = new Vector();
        }

        output.setClip(0, 0, currentApplet.getBufferWidth(), currentApplet.getBufferHeight());
    }

    public final void paintAll() {
        Graphics output = currentApplet.getBackgroundGraphics();

        for (int i = 0; i < sprites.size(); i++) {
            output.setClip(0, 0, currentApplet.getBufferWidth(), currentApplet.getBufferHeight());
            ((Sprite) sprites.elementAt(i)).paint(output, currentApplet);
        }

        output.setClip(0, 0, currentApplet.getBufferWidth(), currentApplet.getBufferHeight());
    }

    public abstract void play(int[] keyCodes, Point mouseCoord, boolean leftButton, boolean rightButton);
}