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

@SuppressWarnings("unused")
public class ScrollingTextSprite extends Sprite
{
	private GameFont font;
	private int scrollSpeed;

	private String text;
	private int startPosition;
	
	private String realText;
	private int realStartPosition;

	private int rightMargin;
	private int leftMargin;
	
	private int lastCharIndex;
	private int firstCharIndex;
	
	public ScrollingTextSprite(Rectangle spriteArea, GameFont font1, String text1, int scrollSpeed1)
	{
		super(spriteArea);
		
		this.font = font1;
		this.text = text1.toLowerCase();
		this.scrollSpeed = scrollSpeed1;
		
		this.startPosition = spriteArea.width;
		this.realStartPosition = spriteArea.width;

		if (text1.length() > 0)
		{		
			this.realText = new String();
		
			this.leftMargin = spriteArea.width+font1.charSize(text1.charAt(0));
			this.rightMargin = 0;
		}
	}
	
	@Override
	public void paint(Graphics g, GameApplet applet)
	{
		if (text.length() == 0)
		{
			return;
		}
		
		startPosition -= scrollSpeed;
		realStartPosition -= scrollSpeed;
		
		rightMargin -= scrollSpeed;
		while (rightMargin < 0)
		{			
			if (lastCharIndex < text.length())
			{	
				int margin = font.charSize(text.charAt(lastCharIndex));
				
				if (margin != 0)
				{
					realText += text.substring(lastCharIndex, lastCharIndex+1);
					rightMargin += font.SEPARATOR_WIDTH;
					rightMargin += margin;			
				}
								
				lastCharIndex++;
			}
			else
			{
				rightMargin = 0;
			}
		}

		leftMargin -= scrollSpeed;
		while (leftMargin < 0)
		{		
			int oldCharSize = font.SEPARATOR_WIDTH+font.charSize(realText.charAt(0));
			
			realText = realText.substring(1);
			
			if (realText.length() == 0)
			{
				leftMargin = getSpriteArea().width+font.charSize(text.charAt(0));
				rightMargin = 0;
				
				startPosition = getSpriteArea().width;
				realStartPosition = getSpriteArea().width;
				
				firstCharIndex = 0;
				lastCharIndex = 0;
			}
			else
			{
				realStartPosition += oldCharSize;
				leftMargin += font.SEPARATOR_WIDTH+font.charSize(realText.charAt(0));
			}
		}
		
		int currentPosition = realStartPosition+getSpriteArea().x;

		for (int i=0 ; i<realText.length() ; i++)
		{
			g.setClip(getSpriteArea());
			currentPosition += font.paintChar(realText.charAt(i), g, new Point(currentPosition, getSpritePosition().y));			
		}
		
		forceRefresh();
	}
}