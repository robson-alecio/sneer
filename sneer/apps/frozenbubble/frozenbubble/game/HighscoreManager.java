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


public class HighscoreManager
{
	private int levelCompleted;
	private long startTime;
	
	private long totalTime;
	private int totalBubbles;
	
	public HighscoreManager() {        
        reset();
	}
	
    public void reset() {
        levelCompleted = 0;
        totalTime = 0;
        totalBubbles = 0;
    }
        
	public void startLevel() {
            startTime = System.currentTimeMillis();
	}
	
	public void endLevel(int nbBubbles) {
        totalTime += System.currentTimeMillis() - startTime;
        totalBubbles += nbBubbles;
        levelCompleted++;	
	}
	
	public String getParameters(String player) {
		return "item=fb&name=" + convertToHTML(player) 
			+ "&score=" + String.valueOf(levelCompleted) 
			+ "&subscore=" + String.valueOf(86400000 - totalTime);
	}
	
	private String convertToHTML(String input) {
        String output = new String();
	
        int begin = 0;
        int end = input.indexOf(" ");
	
        while (end != -1) {
			output += input.substring(begin, end);
			output += "%20";
				
			begin = end + 1;
			end = input.indexOf(" ", begin);
        }	
		
        output += input.substring(begin);
	
        return output;
	}
}