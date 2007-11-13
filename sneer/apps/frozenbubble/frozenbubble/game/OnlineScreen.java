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

public class OnlineScreen extends GameScreen {
	
	private final static int STATE_NO_INIT = 0;
	private final static int STATE_LOADING = 1;
	private final static int STATE_LOADED = 2;
	
	private Image fullBackground;
	private Image splashBackground;
	private Image emptyBoard;
	
	private GameFont font;
	
	private TextSprite loadingText;
	
	private XmlRequest request;
	
	private boolean initKeyboard;
	
	private int state;
	
	public OnlineScreen(GameApplet gameApplet) {
		super(gameApplet);
		
		GameMedia media = gameApplet.getGameMedia();
		
		fullBackground = media.loadImage("background.jpg");
		splashBackground = media.loadImage("splash.jpg");
		emptyBoard = media.loadImage("void_panel.jpg");
		
		font = (GameFont)gameApplet.getGameContext().getObject("bubbleFont");
				
		ScrollingTextSprite scrollingMessage = new ScrollingTextSprite(new Rectangle(168, 9, 300, 22), font, "| Frozen bubble { online highscores |", 1);
		this.addSprite(scrollingMessage);
		
		loadingText = new TextSprite(new Rectangle(275, 155, 180, 22), font, "loading...");
		
		// Retrieve player name
		String playerName = gameApplet.getParameter(FrozenGame.PARAMETER_PLAYER);
		if (playerName == null) {
			playerName = (String) gameApplet.getGameContext().getObject(FrozenGame.PARAMETER_PLAYER);
		}
		
		// Retrive highscore manager
		HighscoreManager highscoreManager = (HighscoreManager)gameApplet.getGameContext().getObject("highscoreManager");
		
		// send highscore
		request = new XmlRequest(gameApplet);
		request.open("POST", "/v2/storeScore.php", true);
		request.send(highscoreManager.getParameters(playerName));
		
		state = STATE_NO_INIT;
		initKeyboard = false;
	}
	
	@Override
	public void initBackground() {
		addToBackground(fullBackground, new Point(0, 0));
		addToBackground(splashBackground, new Point(116, 0));	
		addToBackground(emptyBoard, new Point(180, 98));	
	}	
	
	@Override
	public void play(int[] keyCodes, Point mouseCoord, boolean leftButton, boolean rightButton) {
		
		// Keyboard management
		if (keyCodes.length == 0 && !initKeyboard) {
			initKeyboard = true;
		}
		
		if (initKeyboard) {
			for (int i=0 ; i<keyCodes.length ; i++)	{
				int current = keyCodes[i];
									
				if (current == FrozenGame.KEY_UP || current == FrozenGame.KEY_SHIFT) {
					getGameApplet().setCurrentScreen(new SplashScreen(getGameApplet()));
				}
			}
		}
		
		// Request management
		if (state == STATE_NO_INIT) {
			if (!request.completed()) {
				// Draw 'loading...' only if request is not yet completed
				addSprite(loadingText);
			}
			
			state = STATE_LOADING;
		}
		
		if (state == STATE_LOADING && request.completed()) {
			if (request.getState() == XmlRequest.STATE_OK) {
				// Remove loading
				removeSprite(loadingText);
				
				// Add new data
				MiniDOM answer = request.getResponseXML();
				if (answer == null) {
					addSprite(new TextSprite(new Rectangle(230, 155, 280, 22), font, "invalid answer..."));
				}
				else {
					// Today
					addSprite(new TextSprite(new Rectangle(260, 115, 200, 22), font, "today rank"));
					MiniDOM today = answer.getChild("today");
					String todayRank = "-";
					if (today != null) {
						today = today.getChild("rank");
						if (today != null) {
							todayRank = today.getAttribute("index");
							if (todayRank == null) {
								todayRank = "-";
							}
						}
					}
					addSprite(new TextSprite(new Rectangle(320 - (getTextSize(todayRank, font) >> 1), 145, 120, 22), font, todayRank));
					
					// All time					
					addSprite(new TextSprite(new Rectangle(240, 175, 200, 22), font, "all time rank"));
					MiniDOM allTime = answer.getChild("all");
					String allTimeRank = "-";
					if (allTime != null) {
						allTime = allTime.getChild("rank");
						if (allTime != null) {
							allTimeRank = allTime.getAttribute("index");
							if (allTimeRank == null) {
								allTimeRank = "-";
							}
						}
					}
					addSprite(new TextSprite(new Rectangle(320 - (getTextSize(allTimeRank, font) >> 1), 205, 120, 22), font, allTimeRank));		
				}
			}
			else {
				removeSprite(loadingText);
				addSprite(new TextSprite(new Rectangle(210, 155, 280, 22), font, "connection error..."));
			}
			
			state = STATE_LOADED;
		}
	}
	
	public int getTextSize(String text, GameFont font1) {
		int size = 0;
		
		for (int i=0 ; i<text.length() ; i++) {
			size += font1.charSize(text.charAt(i));
			size += font1.SEPARATOR_WIDTH;
		}
		
		return size - font1.SEPARATOR_WIDTH;
	}	
}