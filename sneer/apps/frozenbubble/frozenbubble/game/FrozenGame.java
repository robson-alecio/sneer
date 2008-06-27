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
import java.util.Random;

import frozenbubble.game.net.library.jiga.*;

public class FrozenGame extends GameScreen
{
	public final static int HORIZONTAL_MOVE = 0;
	public final static int FIRE = 1;
	
	public final static int KEY_UP = 38;
	public final static int KEY_LEFT = 37;
	public final static int KEY_RIGHT = 39;
	public final static int KEY_SHIFT = 16;
        
    public static String PARAMETER_PLAYER = "player";
    public static String PARAMETER_OFFLINE = "offline";
    
    // Change mode (normal/colorblind)
    public final static int KEY_M = 77;
    // Toggle sound on/off
    public final static int KEY_S = 83;

    boolean modeKeyPressed, soundKeyPressed;
        
	Image background;
	Image[] bubbles;
    Image[] bubblesBlind;
	Image[] frozenBubbles;
	Image[] targetedBubbles;
	Random random;
	
	LaunchBubbleSprite launchBubble;
	int launchBubblePosition;
	
	PenguinSprite penguin;
	
	Compressor compressor;
	
	ImageSprite nextBubble;
	int currentColor, nextColor;
	
	BubbleSprite movingBubble;
	BubbleManager bubbleManager;
	LevelManager levelManager;
	LifeManager lifeManager;
	HighscoreManager highscoreManager;
	
	@SuppressWarnings("unchecked")
	Vector jumping;
	@SuppressWarnings("unchecked")
	Vector falling;
	
	BubbleSprite[][] bubblePlay;
	
	int fixedBubbles;
	double moveDown;
	
	Image gameWon, gameLost;
	int nbBubbles;
	
	Image bubbleBlink;
	int blinkDelay;
	
	ImageSprite hurrySprite;
	int hurryTime;
	
	SoundManager soundManager;
	
	boolean readyToFire;
	boolean endOfGame;
	boolean frozenify;
	int frozenifyX, frozenifyY;
	
	@SuppressWarnings("unchecked")
	public FrozenGame(GameApplet gameApplet)
	{
		super(gameApplet);
		
		GameMedia media = gameApplet.getGameMedia();
		random = new Random(System.currentTimeMillis());
		
		background = media.loadImage("background.jpg");
		bubbles = new Image[8];
                bubblesBlind = new Image[8];
		frozenBubbles = new Image[8];
		for (int i=0 ; i<8 ; i++)
		{
			bubbles[i] = media.loadImage("bubble-"+Integer.toString(i+1)+".gif");
                        bubblesBlind[i] = media.loadImage("bubble-colourblind-"+Integer.toString(i+1)+".gif");
			frozenBubbles[i] = media.loadImage("frozen-"+Integer.toString(i+1)+".gif");
		}
		targetedBubbles = new Image[6];
		for (int i=0 ; i<6 ; i++)
		{
			targetedBubbles[i] = media.loadImage("fixed-"+Integer.toString(i+1)+".gif");
		}
		bubbleBlink = media.loadImage("bubble-blink.gif");
		gameWon = media.loadImage("win_panel.jpg");
		gameLost = media.loadImage("lose_panel.jpg");

		soundManager = (SoundManager)gameApplet.getGameContext().getObject("soundManager");
		
		launchBubblePosition = 40;
				
		penguin = new PenguinSprite(media.loadImage("penguins.jpg"), random);
		this.addSprite(penguin);

		compressor = new Compressor(media, this);
			
		hurrySprite = new ImageSprite(new Rectangle(203, 265, 240, 90), media.loadImage("hurry.gif"));
		
		jumping = new Vector();
		falling = new Vector();
		
		bubblePlay = new BubbleSprite[8][13];
		
		bubbleManager = new BubbleManager(bubbles);
		levelManager = (LevelManager)gameApplet.getGameContext().getObject("levelManager");
		byte[][] currentLevel = levelManager.getCurrentLevel();
		
		if (currentLevel == null)
		{
			System.err.println("Level not available");
			return;
		}
		
		for (int j=0 ; j<12 ; j++)
		{
			for (int i=j%2 ; i<8 ; i++)
			{
				if (currentLevel[i][j] != -1)
				{
					BubbleSprite newOne = new BubbleSprite(new Rectangle(190+i*32-(j%2)*16, 44+j*28, 32, 32), 
														   bubbles[currentLevel[i][j]], bubblesBlind[currentLevel[i][j]], frozenBubbles[currentLevel[i][j]],
														   bubbleBlink, bubbleManager, soundManager, this);
														   
					bubblePlay[i][j] = newOne;
					this.addSprite(newOne);
				}
			}
		}
		
		currentColor = bubbleManager.nextBubbleIndex(random);
		nextColor = bubbleManager.nextBubbleIndex(random);
		
		if (FrozenBubble.getMode() == FrozenBubble.GAME_NORMAL) {
            nextBubble = new ImageSprite(new Rectangle(302, 440, 32, 32), bubbles[nextColor]);
        }
        else {
            nextBubble = new ImageSprite(new Rectangle(302, 440, 32, 32), bubblesBlind[nextColor]);
        }
		this.addSprite(nextBubble);
		
		launchBubble = new LaunchBubbleSprite(gameApplet, currentColor, launchBubblePosition >> 1);

		this.spriteToBack(launchBubble);
		
		BubbleFont bubbleFont = (BubbleFont)gameApplet.getGameContext().getObject("bubbleFont");
		
		TextSprite levelNumber = null;
		
		if (levelManager.getLevelIndex() < 10)
		{
			levelNumber = new TextSprite(new Rectangle(175, 433, 21, 22), bubbleFont, String.valueOf(levelManager.getLevelIndex()));
		}
		else if (levelManager.getLevelIndex() < 100)
		{
			levelNumber = new TextSprite(new Rectangle(168, 433, 43, 22), bubbleFont, String.valueOf(levelManager.getLevelIndex()));
		}
		else
		{
			levelNumber = new TextSprite(new Rectangle(163, 433, 65, 22), bubbleFont, String.valueOf(levelManager.getLevelIndex()));
		}
		
		this.spriteToBack(levelNumber);
		
		lifeManager = (LifeManager)gameApplet.getGameContext().getObject("lifeManager");
		this.spriteToBack(lifeManager);
		
		highscoreManager = (HighscoreManager)gameApplet.getGameContext().getObject("highscoreManager");
		
		nbBubbles = 0;		
	}
	
	private void initFrozenify()
	{
		ImageSprite freezeLaunchBubble = new ImageSprite(new Rectangle(301, 389, 34, 42), frozenBubbles[currentColor]);
		ImageSprite freezeNextBubble = new ImageSprite(new Rectangle(301, 439, 34, 42), frozenBubbles[nextColor]);
		
		this.addSprite(freezeLaunchBubble);
		this.addSprite(freezeNextBubble);
		
		frozenifyX = 7;
		frozenifyY = 12;
		
		frozenify = true;
	}

	private void frozenify()
	{	
		frozenifyX--;
		if (frozenifyX < 0)
		{
			frozenifyX = 7;
			frozenifyY--;
			
			if (frozenifyY<0)
			{
				frozenify = false;
				this.addSprite(new ImageSprite(new Rectangle(152, 190, 337, 116), gameLost));
				lifeManager.decrease();
				soundManager.playSound(FrozenBubble.SOUND_NOH);
					
				return;
			}			
		}

		while (bubblePlay[frozenifyX][frozenifyY] == null && frozenifyY >=0)
		{
			frozenifyX--;
			if (frozenifyX < 0)
			{
				frozenifyX = 7;
				frozenifyY--;
				
				if (frozenifyY<0)
				{
					frozenify = false;
					this.addSprite(new ImageSprite(new Rectangle(152, 190, 337, 116), gameLost));
					lifeManager.decrease();
					soundManager.playSound(FrozenBubble.SOUND_NOH);
					
					return;
				}				
			}			
		}
		
		this.spriteToBack(bubblePlay[frozenifyX][frozenifyY]);
		bubblePlay[frozenifyX][frozenifyY].frozenify();
			
		this.spriteToBack(launchBubble);
	}	
	
	public BubbleSprite[][] getGrid()
	{
		return bubblePlay;
	}
	
	@SuppressWarnings("unchecked")
	public void addFallingBubble(BubbleSprite sprite)
	{
		spriteToFront(sprite);
		falling.addElement(sprite);
	}

	public void deleteFallingBubble(BubbleSprite sprite)
	{
		removeSprite(sprite);
		falling.removeElement(sprite);
	}
	
	@SuppressWarnings("unchecked")
	public void addJumpingBubble(BubbleSprite sprite)
	{
		spriteToFront(sprite);
		jumping.addElement(sprite);
	}	

	public void deleteJumpingBubble(BubbleSprite sprite)
	{
		removeSprite(sprite);
		jumping.removeElement(sprite);
	}	
	
	public Random getRandom()
	{
		return random;
	}
	
	public double getMoveDown()
	{
		return moveDown;
	}
	
	private void sendBubblesDown()
	{
		soundManager.playSound(FrozenBubble.SOUND_NEWROOT);
		
		for (int i=0 ; i<8 ; i++)
		{
			for (int j=0 ; j<12 ; j++)
			{
				if (bubblePlay[i][j] != null)
				{
					bubblePlay[i][j].moveDown();
					
					if (bubblePlay[i][j].getSpritePosition().y>=380)
					{
						penguin.updateState(PenguinSprite.STATE_GAME_LOST);
						endOfGame = true;
						initFrozenify();
						
						soundManager.playSound(FrozenBubble.SOUND_LOST);
					}
				}
			}
		}
		
		moveDown += 28.;
		compressor.moveDown();
	}
		
	private void blinkLine(int number)
	{
		int move = number % 2;
		int column = (number+1) >> 1;
		
		for (int i=move ; i<13 ; i++)
		{
			if (bubblePlay[column][i] != null)
			{
				bubblePlay[column][i].blink();
			}
		}
	}
	
	@Override
	public void initBackground()
	{		
		if (lifeManager.isDead())
		{
			if (levelManager.getLevelIndex() == 1) {
                this.getGameApplet().setCurrentScreen(new SplashScreen(getGameApplet()));
			}
			else {
                if (this.getGameApplet().getParameter(PARAMETER_OFFLINE) == null && !this.getGameApplet().isApplication()) {
                    String playerName = this.getGameApplet().getParameter(PARAMETER_PLAYER);
                    
                    if (playerName == null) {
                        this.getGameApplet().setCurrentScreen(new EnterNameScreen(getGameApplet()));
                    }
                    else {
                        if (!"".equals(playerName.trim())) {
                            this.getGameApplet().setCurrentScreen(new OnlineScreen(getGameApplet()));
                        }
                        else {
                        	// Player name set but null
                        	this.getGameApplet().setCurrentScreen(new OfflineScreen(getGameApplet()));
                        }
                    }
                }
                else {
                    this.getGameApplet().setCurrentScreen(new OfflineScreen(getGameApplet()));
                }
			}
		}		
		else
		{
			addToBackground(background, new Point(0, 0));
			
			highscoreManager.startLevel();
		}
	}
	
	@Override
	public void play(int[] keyCodes, Point mouseCoord, boolean leftButton, boolean rightButton) 
	{
		int[] move = new int[2];
		boolean newModeKeyState = false;
        boolean newSoundKeyState = false;
                
		for (int i=0 ; i<keyCodes.length ; i++)
		{
			int current = keyCodes[i];

            if (current == KEY_M) {
                newModeKeyState = true;
            }

            if (current == KEY_S) {
                newSoundKeyState = true;
            }                        
                        
			if (current == KEY_LEFT || current == KEY_RIGHT)
			{
				if (move[HORIZONTAL_MOVE] == 0)
				{
					move[HORIZONTAL_MOVE] = current;
				}
				else
				{
					move[HORIZONTAL_MOVE] = 0;
				}
			}
			
			if (current == KEY_UP || current == KEY_SHIFT)
			{
				move[FIRE] = KEY_UP;
			}
		}
		
        if (newModeKeyState != modeKeyPressed) {
            if (newModeKeyState) {
                FrozenBubble.switchMode();
                // Redraw nextBubble
                if (FrozenBubble.getMode() == FrozenBubble.GAME_NORMAL) {
                    nextBubble.changeImage(bubbles[nextColor]);
                }
                else {
                    nextBubble.changeImage(bubblesBlind[nextColor]);
                }                        
            }
            
            modeKeyPressed = newModeKeyState;
        }
        
        if (newSoundKeyState != soundKeyPressed) {
            if (newSoundKeyState) {
                soundManager.setSoundState(!soundManager.getSoundState());
            }
            
            soundKeyPressed = newSoundKeyState;
        }                
                
		if (move[FIRE] == 0)
		{
			readyToFire = true;
		}
		
		if (endOfGame)
		{
			if (move[FIRE] == KEY_UP && readyToFire)
			{
				if (frozenify)
				{
					lifeManager.decrease();
				}
				
				if (levelManager.getCurrentLevel() == null)
				{
					getGameApplet().setCurrentScreen(new EnterNameScreen(getGameApplet()));
				}
				else
				{
					getGameApplet().setCurrentScreen(new FrozenGame(getGameApplet()));
				}
			}
			else
			{
				penguin.updateState(PenguinSprite.STATE_VOID);
			
				if (frozenify)
				{
					frozenify();
				}
			}
		}
		else
		{						
			if (move[FIRE] == KEY_UP || hurryTime > 480)
			{
				if (movingBubble == null && readyToFire)
				{
					nbBubbles++;
					
					movingBubble = new BubbleSprite(new Rectangle(302, 390, 32, 32), launchBubblePosition >> 1, bubbles[currentColor], bubblesBlind[currentColor], 
													frozenBubbles[currentColor], targetedBubbles, bubbleBlink, bubbleManager, soundManager, this);
					this.addSprite(movingBubble);
				
					currentColor = nextColor;
					nextColor = bubbleManager.nextBubbleIndex(random);
                                        
                    if (FrozenBubble.getMode() == FrozenBubble.GAME_NORMAL) {
                        nextBubble.changeImage(bubbles[nextColor]);
                    }
                    else {
                        nextBubble.changeImage(bubblesBlind[nextColor]);
                    }
					launchBubble.changeColor(currentColor);
					penguin.updateState(PenguinSprite.STATE_FIRE);
					
					soundManager.playSound(FrozenBubble.SOUND_LAUNCH);
					
					readyToFire = false;
					hurryTime = 0;
					removeSprite(hurrySprite);
				}	
				else
				{
					penguin.updateState(PenguinSprite.STATE_VOID);
				}
			}
			else if (move[HORIZONTAL_MOVE] == KEY_LEFT)
			{
				if (launchBubblePosition > 2)
				{
					launchBubblePosition--;
				}
				launchBubble.changeDirection(launchBubblePosition >> 1);
				penguin.updateState(PenguinSprite.STATE_TURN_LEFT);
			}
			else if (move[HORIZONTAL_MOVE] == KEY_RIGHT)
			{
				if (launchBubblePosition<78)
				{
					launchBubblePosition++;
				}
				launchBubble.changeDirection(launchBubblePosition >> 1);
				penguin.updateState(PenguinSprite.STATE_TURN_RIGHT);
			}
			else
			{
				penguin.updateState(PenguinSprite.STATE_VOID);
			}
		}
		
		if (movingBubble != null)
		{
			movingBubble.move();
			if (movingBubble.fixed())
			{
				if (movingBubble.getSpritePosition().y>=380 && !movingBubble.released())
				{
					penguin.updateState(PenguinSprite.STATE_GAME_LOST);
					endOfGame = true;
					initFrozenify();
					
					soundManager.playSound(FrozenBubble.SOUND_LOST);
				}
				else if (bubbleManager.countBubbles() == 0)
				{
					penguin.updateState(PenguinSprite.STATE_GAME_WON);
					this.addSprite(new ImageSprite(new Rectangle(152, 190, 337, 116), gameWon));
					highscoreManager.endLevel(nbBubbles);
					levelManager.goToNextLevel();
					endOfGame = true;
					
					soundManager.playSound(FrozenBubble.SOUND_WON);
				}
				else
				{
					fixedBubbles++;
					blinkDelay = 0;
					
					if (fixedBubbles == 8)
					{
						fixedBubbles = 0;
						sendBubblesDown();
					}					
				}
				
				movingBubble = null;
			}
			
			if (movingBubble != null)
			{
				movingBubble.move();
				if (movingBubble.fixed())
				{					
					if (movingBubble.getSpritePosition().y>=380 && !movingBubble.released())
					{
						penguin.updateState(PenguinSprite.STATE_GAME_LOST);
						endOfGame = true;
						initFrozenify();
						
						soundManager.playSound(FrozenBubble.SOUND_LOST);
					}
					else if (bubbleManager.countBubbles() == 0)
					{
						penguin.updateState(PenguinSprite.STATE_GAME_WON);
						this.addSprite(new ImageSprite(new Rectangle(152, 190, 337, 116), gameWon));
						highscoreManager.endLevel(nbBubbles);
						levelManager.goToNextLevel();
						endOfGame = true;
						
						soundManager.playSound(FrozenBubble.SOUND_WON);
					}
					else
					{
						fixedBubbles++;
						blinkDelay = 0;
						
						if (fixedBubbles == 8)
						{
							fixedBubbles = 0;
							sendBubblesDown();
						}
					}
					
					movingBubble = null;
				}
			}			
		}
		
		if (movingBubble == null && !endOfGame)
		{
			hurryTime++;
			
			if (hurryTime >= 240)
			{
				if (hurryTime % 40 == 10)
				{
					addSprite(hurrySprite);
					soundManager.playSound(FrozenBubble.SOUND_HURRY);
				}
				else if (hurryTime % 40 == 35)
				{
					removeSprite(hurrySprite);
				}
			}
		}
		
		if (fixedBubbles == 6)
		{
			if (blinkDelay < 15)
			{
				blinkLine(blinkDelay);
			}

			blinkDelay++;
			if (blinkDelay == 40)
			{
				blinkDelay = 0;
			}
		}
		else if (fixedBubbles == 7)
		{
			if (blinkDelay < 15)
			{
				blinkLine(blinkDelay);
			}

			blinkDelay++;
			if (blinkDelay == 25)
			{
				blinkDelay = 0;
			}
		}		
		
		for (int i=0 ; i<falling.size() ; i++)
		{
			((BubbleSprite)falling.elementAt(i)).fall();
		}
		
		for (int i=0 ; i<jumping.size() ; i++)
		{
			((BubbleSprite)jumping.elementAt(i)).jump();
		}	
	}
}