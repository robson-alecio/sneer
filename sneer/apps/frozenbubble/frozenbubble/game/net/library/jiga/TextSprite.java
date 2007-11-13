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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;

public class TextSprite extends Sprite
{
	private GameFont font;
	private String text;
	
	private int lastCharacter;
	
	public TextSprite(Rectangle spriteArea, GameFont font1, String initialText)
	{
		super(spriteArea);
		
		this.font = font1;
		this.text = initialText.toLowerCase();
		this.lastCharacter = findLastCharacter(text);
	}
	
	private int findLastCharacter(String text1)
	{
		int fitsIn = getSpriteArea().width;
		int currentSize = 0;

		char[] array = text1.toCharArray();
		
		for (int i=0 ; i<array.length ; i++)
		{                    
			currentSize += font.charSize(array[i])+font.SEPARATOR_WIDTH;
			
			if (currentSize >= fitsIn)
			{
				return i;
			}
		}
		
		return array.length-1;
	}
	
	public String getText()
	{
		return text;
	}	
	
	public GameFont getFont()
	{
		return font;
	}
	
	public void addCharacter(char c)
	{
		text += String.valueOf(c).toLowerCase();
		lastCharacter = findLastCharacter(text);
	}
	
	public void removeLastChar()
	{
		text = text.substring(0, text.length()-1);
		lastCharacter = findLastCharacter(text);
		forceRefresh();
	}
	
	@Override
	public void paint(Graphics g, GameApplet applet)
	{
		int currentPosition = getSpritePosition().x;
		
		for (int i=0 ; i<=lastCharacter ; i++)
		{
			g.setClip(getSpriteArea());
			currentPosition += font.paintChar(text.charAt(i), g, new Point(currentPosition, getSpritePosition().y));
		}
	}
}