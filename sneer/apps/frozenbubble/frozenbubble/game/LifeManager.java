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

public class LifeManager extends Sprite
{
	public final static int NB_LIVES = 4;
	
	private int currentLife;
	private Image penguin;
	
	public LifeManager(GameApplet applet)
	{
		super(new Rectangle(529, 3, 107, 35));
		
		penguin = applet.getGameMedia().loadImage("life.gif");
		
		currentLife = NB_LIVES;
	}
	
	public final void restart()
	{
		currentLife = NB_LIVES;
	}
	
	public final void decrease()
	{
		currentLife--;
		forceRefresh();
	}
	
	public final boolean isDead()
	{
		return currentLife <= 0;
	}
	
	@Override
	public final void paint(Graphics g, GameApplet applet)
	{
		for (int i=0 ; i<currentLife ; i++)
		{
			g.drawImage(penguin, 601-i*36, 3, applet);
		}
	}
}