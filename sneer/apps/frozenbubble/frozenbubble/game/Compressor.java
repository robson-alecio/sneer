package frozenbubble.game;

/*
 *                 [[ Frozen-Bubble ]]
 *
 * Copyright (c) 2000-2003 Guillaume Cottenceau.
 * Java sourcecode - Copyright (c) 2003 Glenn Sanson.
 *
 * This code is distributed under the GNU General Public License 
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2, as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *
 * Artwork:
 *    Alexis Younes <73lab at free.fr>
 *      (everything but the bubbles)
 *    Amaury Amblard-Ladurantie <amaury at linuxfr.org>
 *      (the bubbles)
 *
 * Soundtrack:
 *    Matthias Le Bidan <matthias.le_bidan at caramail.com>
 *      (the three musics and all the sound effects)
 *
 * Design & Programming:
 *    Guillaume Cottenceau <guillaume.cottenceau at free.fr>
 *      (design and manage the project, whole Perl sourcecode)
 *
 * Java version:
 *    Glenn Sanson <glenn.sanson at free.fr>
 *      (whole Java sourcecode, including JIGA classes 
 *             http://glenn.sanson.free.fr/jiga/)
 *
 *          [[ http://glenn.sanson.free.fr/fb/ ]]
 *          [[ http://www.frozen-bubble.org/   ]]
 */


import java.awt.*;

import frozenbubble.game.net.library.jiga.*;

@SuppressWarnings("unused")
public class Compressor
{
	private ImageSprite compressorHead;
	private Image compressorBody;
	
	private GameMedia media;
	private GameScreen screen;
		
	public Compressor(GameMedia media1, GameScreen screen1)
	{	
		this.media = media1;
		this.screen = screen1;
		
		compressorHead = new ImageSprite(new Rectangle(160, -7, 321, 51),
										 media1.loadImage("compressor.gif"));
		
		compressorBody = media1.loadImage("compressorBody.gif");
		
		screen1.addSprite(compressorHead);
	}
	
	public void moveDown()
	{		
		screen.addToBackground(compressorBody, new Point(compressorHead.getSpritePosition().x+75, compressorHead.getSpritePosition().y+3));
		screen.addToBackground(compressorBody, new Point(compressorHead.getSpritePosition().x+231, compressorHead.getSpritePosition().y+3));
		
		compressorHead.relativeMove(new Point(0, 28));
	}
}