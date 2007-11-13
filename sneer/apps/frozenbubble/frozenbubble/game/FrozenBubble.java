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

import frozenbubble.game.net.library.jiga.*;

public class FrozenBubble extends GameApplet {
	
	private static final long serialVersionUID = 8384765702488049691L;

	public final static int SOUND_WON = 0;
	public final static int SOUND_LOST = 1;
	public final static int SOUND_LAUNCH = 2;
	public final static int SOUND_DESTROY = 3;
	public final static int SOUND_REBOUND = 4;
	public final static int SOUND_STICK = 5;
	public final static int SOUND_HURRY = 6;
	public final static int SOUND_NEWROOT = 7;
	public final static int SOUND_NOH = 8;
	public final static int SOUND_TYPEWRITER = 9;
	
	private final static String[] SOUND_FILES = {"applause.au", "lose.au", "launch.au", 
												 "destroy_group.au", "rebound.au", "stick.au", 
												 "hurry.au", "newroot_solo.au", "noh.au",
												 "typewriter.au"};
	
    public final static int GAME_NORMAL = 0;
    public final static int GAME_COLORBLIND = 1;
                                                                                             
    private static int gameMode;
                                                                                                 
	@Override
	public void gameInit() {
		
		LevelManager levelManager = new LevelManager(this.getGameMedia().loadData("levels.txt"));
		this.getGameContext().addObject("levelManager", levelManager);
		
		SoundManager soundManager = new SoundManager(this, SOUND_FILES);
		this.getGameContext().addObject("soundManager", soundManager);
		
		BubbleFont bubbleFont = new BubbleFont(this);
		this.getGameContext().addObject("bubbleFont", bubbleFont);
		
		LifeManager lifeManager = new LifeManager(this);
		this.getGameContext().addObject("lifeManager", lifeManager);
		
		HighscoreManager highscoreManager = new HighscoreManager();
		this.getGameContext().addObject("highscoreManager", highscoreManager);
                
		// Init current mode
		setMode(GAME_NORMAL);
                
        // Used to precalc. bubbleLauncher images
		new LaunchBubbleSprite(this, 0, 0);
	}
        
    public static void switchMode() {
        if (gameMode == GAME_COLORBLIND) {
            gameMode = GAME_NORMAL;
        }
        else {
            gameMode = GAME_COLORBLIND;
        }
    }
    
    public static void setMode(int newMode) {
        gameMode = newMode;
    }
    
    public static int getMode() {
        return gameMode;
    }
	
        
	@Override
	public GameScreen getInitialScreen() {
		return new SplashScreen(this);
	}
	
	@Override
	public boolean needsMouseEvents() {
		return false;
	}	

	public static void main(String[] args) {
		new FrozenFrame("Frozen Bubble v1.0.0", new FrozenBubble(), 640, 480);            
    }
}