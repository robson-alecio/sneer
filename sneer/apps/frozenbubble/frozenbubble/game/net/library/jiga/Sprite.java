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

import java.awt.*;

/**
 * Abstract class representing a graphical object in the JIGA Framework.
 * @author Glenn Sanson
 */
public abstract class Sprite
{
	private Rectangle spriteArea;
	private Rectangle oldSpriteArea;
	private boolean refreshNeeded;
	
	/**
	 * Default Sprite constructor
	 * @param spriteArea1 a graphical area containing the object to draw (refresh area)   
	 */
	public Sprite(Rectangle spriteArea1)
	{	
		this.spriteArea = spriteArea1;
		this.oldSpriteArea = spriteArea1;
		refreshNeeded = true;
	}
	
	/**
	 * Forces refresh of the background data 
	 * (for example, when the sprite image change within the same sprite area)
	 */
	public final void forceRefresh()
	{
		refreshNeeded = true;
	}
	
	/**
	 * Defines a new sprite area. This force a background refresh of the old area, 
	 * even if there isn't any real movement or change in size
	 * @param newArea The new sprite area
	 */
	public void changeSpriteArea(Rectangle newArea)
	{
		spriteArea = newArea;
		refreshNeeded = true;
	}
	
	/**
	 * Translates sprite  
	 * @param p a <code>Point</code> representing the movement vector
	 */
	public final void relativeMove(Point p)
	{
		spriteArea = new Rectangle(spriteArea);
		spriteArea.translate(p.x, p.y);
		refreshNeeded = true;
	}

	/**
	 * Translates sprite
	 * @param x the X coordinate of the movement vector
	 * @param y the X coordinate of the movement vector
	 */
	public final void relativeMove(int x, int y)
	{
		spriteArea = new Rectangle(spriteArea);
		spriteArea.translate(x, y);
		refreshNeeded = true;
	}
	
	/**
	 * Translates sprite to a new absolute position
	 * @param p The upper-left corner of the sprite area
	 */
	public final void absoluteMove(Point p)
	{
		spriteArea = new Rectangle(p, new Dimension(spriteArea.width, spriteArea.height));
		refreshNeeded = true;
	}

	/**
	 * Translates sprite to a new absolute position
	 * @param x The min X value of the new sprite area
	 * @param y The min Y value of the new sprite area
	 */
	public final void absoluteMove(int x, int y)
	{
		spriteArea = new Rectangle(x, y, spriteArea.width, spriteArea.height);
		refreshNeeded = true;
	}
	
	/**
	 * Retrieves current sprite position
	 * @return The upper-left corner of the sprite area
	 */
	public final Point getSpritePosition()
	{
		return spriteArea.getLocation();
	}
	
	/**
	 * Retrieves current sprite area
	 * @return The sprite area
	 */
	public final Rectangle getSpriteArea()
	{
		return spriteArea;
	}
	
	/**
	 * Refreshes sprite (Redraws backgound data). The sprite is refreshed only when :
	 * <li><ul>The sprite area has changed
	 * <ul>The <code>forceRefresh()</code> method was called</li>
	 * @param background The background image (current scene without any sprite)
	 * @param output The graphics where the sprite is to be drawn
	 * @param applet A reference to the app(let) main class
	 */
	public void refresh(Image background, Graphics output, GameApplet applet)
	{
		if (refreshNeeded)
		{
			output.setClip(oldSpriteArea.x, oldSpriteArea.y, oldSpriteArea.width, oldSpriteArea.height);
			output.drawImage(background, 0, 0, applet);
			oldSpriteArea = spriteArea;
			
			refreshNeeded = false;
		}
	}
	
	/**
	 * Graphical operations associated with the sprite. 
	 * In most cases, the graphical representation of a sprite will be a simple <code>Image</code> 
	 * @param g The graphics where the sprite is to be drawn
	 * @param applet A reference to the app(let) main class
	 * @see ImageSprite
	 */
	public abstract void paint(Graphics g, GameApplet applet);
}