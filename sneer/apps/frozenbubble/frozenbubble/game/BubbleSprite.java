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
import java.util.Vector;

import frozenbubble.game.net.library.jiga.*;

public class BubbleSprite extends Sprite
{
	private static double FALL_SPEED = 1.;
	private static double MAX_BUBBLE_SPEED = 8.;
	private static double MINIMUM_DISTANCE = 841.;
	
	private Image _bubbleFace;
        private Image _bubbleBlindFace;
	private Image _frozenFace;
	private Image _bubbleBlink;
	private Image[] _bubbleFixed;
	private FrozenGame _frozen;
	private BubbleManager _bubbleManager;
	private double moveX, moveY;
	private double realX, realY;
	
	private boolean fixed;
	private boolean blink;
	private boolean released;

	private boolean checkJump;
	private boolean checkFall;

	private int fixedAnim;
	
	private SoundManager soundManager;
	
	public BubbleSprite(Rectangle area, int direction, Image bubbleFace, Image bubbleBlindFace, Image frozenFace, Image[] bubbleFixed, Image bubbleBlink, BubbleManager bubbleManager, SoundManager soundManager1, FrozenGame frozen)
	{
		super(area);
		
		this._bubbleFace = bubbleFace;
                this._bubbleBlindFace = bubbleBlindFace;
		this._frozenFace = frozenFace;
		this._bubbleFixed = bubbleFixed;
		this._bubbleBlink = bubbleBlink;
		this._bubbleManager = bubbleManager;
		this.soundManager = soundManager1;
		this._frozen = frozen;
		
		this.moveX = MAX_BUBBLE_SPEED * -Math.cos(direction * Math.PI / 40.);
		this.moveY = MAX_BUBBLE_SPEED * -Math.sin(direction * Math.PI / 40.);
		this.realX = area.x;
		this.realY = area.y;
		
		fixed = false;
		fixedAnim = -1;
	}
		
	public BubbleSprite(Rectangle area, Image bubbleFace, Image bubbleBlindFace, Image frozenFace, Image bubbleBlink, BubbleManager bubbleManager, SoundManager soundManager1, FrozenGame frozen)
	{
		super(area);
		
		this._bubbleFace = bubbleFace;
                this._bubbleBlindFace = bubbleBlindFace;
		this._frozenFace = frozenFace;
		this._bubbleBlink = bubbleBlink;
		this._bubbleManager = bubbleManager;
		this.soundManager = soundManager1;
		this._frozen = frozen;
		
		this.realX = area.x;
		this.realY = area.y;
		
		fixed = true;
		fixedAnim = -1;
		bubbleManager.addBubble(bubbleFace);
	}
	
	Point currentPosition()
	{
		int posY = (int)Math.floor((realY-28.-_frozen.getMoveDown())/28.);
		int posX = (int)Math.floor((realX-174.)/32. + 0.5*(posY%2));
		
		if (posX>7) 
		{
			posX = 7;
		}
		
		if (posX<0) 
		{
			posX = 0;
		}
		
		if (posY<0)
		{
			posY = 0;
		}
		
		return new Point(posX, posY);
	}
	
	public void removeFromManager()
	{
		_bubbleManager.removeBubble(_bubbleFace);
	}
	
	public boolean fixed()
	{
		return fixed;
	}
	
	public boolean checked()
	{
		return checkFall;
	}
	
	public boolean released()
	{
		return released;
	}
	
	public void moveDown()
	{
		if (fixed)
		{
			realY += 28.;
		}
		
		super.absoluteMove(new Point((int)realX, (int)realY));
	}
	
	@SuppressWarnings("unchecked")
	public void move()
	{
		realX += moveX;
		
		if (realX>=414.)
		{
			moveX = -moveX;
			realX += (414. - realX);
			soundManager.playSound(FrozenBubble.SOUND_REBOUND);
		}
		else if (realX<=190.)
		{
			moveX = -moveX;
			realX += (190. - realX);
			soundManager.playSound(FrozenBubble.SOUND_REBOUND);
		}
		
		realY += moveY;
		
		Point currentPosition = currentPosition();
		Vector<?> neighbors = getNeighbors(currentPosition);
		
		if (checkCollision(neighbors) || realY < 44.+_frozen.getMoveDown())
		{
			realX = 190.+currentPosition.x*32-(currentPosition.y%2)*16;
			realY = 44.+currentPosition.y*28+_frozen.getMoveDown();
			
			fixed = true;
			
			Vector<?> checkJump2 = new Vector();
			this.checkJump(checkJump2, neighbors);
			
			BubbleSprite[][] grid = _frozen.getGrid();
			
			if (checkJump2.size() >= 3)
			{	
				released = true;
				
				for (int i=0 ; i<checkJump2.size() ; i++)
				{
					BubbleSprite current = (BubbleSprite)checkJump2.elementAt(i);
					Point currentPoint = current.currentPosition();
					
					_frozen.addJumpingBubble(current);
					if (i>0)
					{
						current.removeFromManager();
					}
					grid[currentPoint.x][currentPoint.y] = null;
				}
				
				for (int i=0 ; i<8 ; i++)
				{
					if (grid[i][0] != null)
					{
						grid[i][0].checkFall();
					}
				}
				
				for (int i=0 ; i<8 ; i++)
				{
					for (int j=0 ; j<12 ; j++)
					{
						if (grid[i][j] != null)
						{
							if (!grid[i][j].checked())
							{
								_frozen.addFallingBubble(grid[i][j]);
								grid[i][j].removeFromManager();
								grid[i][j] = null;
							}
						}
					}
				}
				
				soundManager.playSound(FrozenBubble.SOUND_DESTROY);
			}
			else
			{
				_bubbleManager.addBubble(_bubbleFace);
				grid[currentPosition.x][currentPosition.y] = this;
				moveX = 0.;
				moveY = 0.;
				fixedAnim = 0;
				soundManager.playSound(FrozenBubble.SOUND_STICK);
			}
		}
		
		super.absoluteMove(new Point((int)realX, (int)realY));
	}
	
	@SuppressWarnings("unchecked")
	Vector getNeighbors(Point p)
	{
		BubbleSprite[][] grid = _frozen.getGrid();
		
		Vector list = new Vector();
		
		if ((p.y % 2) == 0)
		{
			if (p.x > 0)
			{
				list.addElement(grid[p.x-1][p.y]);
			}
			
			if (p.x < 7)
			{
				list.addElement(grid[p.x+1][p.y]);
				
				if (p.y > 0)
				{
					list.addElement(grid[p.x][p.y-1]);
					list.addElement(grid[p.x+1][p.y-1]);
				}
				
				if (p.y < 12)
				{
					list.addElement(grid[p.x][p.y+1]);
					list.addElement(grid[p.x+1][p.y+1]);
				}
			}
			else
			{
				if (p.y > 0)
				{
					list.addElement(grid[p.x][p.y-1]);
				}
				
				if (p.y < 12)
				{
					list.addElement(grid[p.x][p.y+1]);
				}				
			}
		}
		else
		{
			if (p.x < 7)
			{
				list.addElement(grid[p.x+1][p.y]);
			}
			
			if (p.x > 0)
			{
				list.addElement(grid[p.x-1][p.y]);
				
				if (p.y > 0)
				{
					list.addElement(grid[p.x][p.y-1]);
					list.addElement(grid[p.x-1][p.y-1]);
				}
				
				if (p.y < 12)
				{
					list.addElement(grid[p.x][p.y+1]);
					list.addElement(grid[p.x-1][p.y+1]);
				}
			}
			else
			{
				if (p.y > 0)
				{
					list.addElement(grid[p.x][p.y-1]);
				}
				
				if (p.y < 12)
				{
					list.addElement(grid[p.x][p.y+1]);
				}				
			}			
		}
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	void checkJump(Vector jump, Image compare)
	{
		if (checkJump)
		{
			return;
		}
		checkJump = true;
		
		if (this._bubbleFace == compare)
		{
			checkJump(jump, this.getNeighbors(this.currentPosition()));	
		}
	}
	
	@SuppressWarnings("unchecked")
	void checkJump(Vector jump, Vector neighbors)
	{
		jump.addElement(this);
		
		for (int i=0 ; i<neighbors.size() ; i++)
		{
			BubbleSprite current = (BubbleSprite)neighbors.elementAt(i);
			
			if (current != null)
			{
				current.checkJump(jump, this._bubbleFace);
			}
		}		
	}
		
	@SuppressWarnings("unchecked")
	public void checkFall()
	{
		if (checkFall)
		{
			return;
		}
		checkFall = true;
		
		Vector v = this.getNeighbors(this.currentPosition());
				
		for (int i=0 ; i<v.size() ; i++)
		{
			BubbleSprite current = (BubbleSprite)v.elementAt(i);
			
			if (current != null)
			{
				current.checkFall();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	boolean checkCollision(Vector neighbors)
	{
		for (int i=0 ; i<neighbors.size() ; i++)
		{
			BubbleSprite current = (BubbleSprite)neighbors.elementAt(i);
			
			if (current != null)
			{
				if (checkCollision(current))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	boolean checkCollision(BubbleSprite sprite)
	{
		double value = (sprite.getSpriteArea().x - this.realX) * (sprite.getSpriteArea().x - this.realX)
					   + (sprite.getSpriteArea().y - this.realY) * (sprite.getSpriteArea().y - this.realY);
		
		return (value < MINIMUM_DISTANCE);
	}
	
	public void jump()
	{
		if (fixed)
		{
			moveX = -6. + _frozen.getRandom().nextDouble() * 12.;
			moveY = -5. - _frozen.getRandom().nextDouble() * 10. ;
			
			fixed = false;
		}
		
		moveY += FALL_SPEED;
		realY += moveY;
		realX += moveX;
		
		super.absoluteMove(new Point((int)realX, (int)realY));
		
		if (realY >= 680.)
		{
			_frozen.deleteJumpingBubble(this);
		}
	}
	
	public void fall()
	{
		if (fixed)
		{
			moveY = _frozen.getRandom().nextDouble()* 5.;
		}
		
		fixed = false;
		
		moveY += FALL_SPEED;
		realY += moveY;
		
		super.absoluteMove(new Point((int)realX, (int)realY));
		
		if (realY >= 680.)
		{
			_frozen.deleteFallingBubble(this);
		}
	}
	
	public void blink()
	{
		blink = true;
	}
	
	public void frozenify()
	{
		changeSpriteArea(new Rectangle(getSpritePosition().x-1, getSpritePosition().y-1, 34, 42));
		_bubbleFace = _frozenFace;
	}
	
	@Override
	public final void paint(Graphics g, GameApplet applet)
	{
		checkJump = false;
		checkFall = false;
		
		Point p = getSpritePosition();
		
		if (blink && _bubbleFace != _frozenFace)
		{
			blink = false;
			g.drawImage(_bubbleBlink, p.x, p.y, applet);
		}
		else
		{
            if (FrozenBubble.getMode() == FrozenBubble.GAME_NORMAL || _bubbleFace == _frozenFace) {
                g.drawImage(_bubbleFace, p.x, p.y, applet);
            }
            else {
                g.drawImage(_bubbleBlindFace, p.x, p.y, applet);
            }
		}
		
		if (fixedAnim != -1)
		{
			g.drawImage(_bubbleFixed[fixedAnim], p.x, p.y, applet);
			fixedAnim++;
			if (fixedAnim == 6)
			{
				fixedAnim = -1;
			}
		}
	}	
}