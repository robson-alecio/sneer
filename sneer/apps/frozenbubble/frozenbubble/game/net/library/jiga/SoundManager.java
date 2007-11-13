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

import java.applet.*;

public class SoundManager
{	
	private final String[] AUDIO_CLIP_VENDORS = {"Sun Microsystems Inc.", "Microsoft Corp.", "Apple Computer, Inc."};
	private final String[] AUDIO_STREAM_VENDORS = {"Netscape Communications Corporation"};
	
	private final int NO_SOUND = 0;
	private final int AUDIO_CLIP = 1;
	private final int AUDIO_STREAM = 2;
	
	private AudioClip[] managedAudioClips;
	private SunAudioManager sunAudio;
	
	private boolean playSounds;
	private int audioType;
	
	public SoundManager(GameApplet applet, String[] files)
	{
		this(applet, files, true);	
	}

	
	public SoundManager(GameApplet applet, String[] files, boolean soundEnabled)
	{
		this.sunAudio = applet.getSunAudioManager();
		this.playSounds = soundEnabled;
		this.audioType = getVendorType();
		
		if (files == null)
		{
			files = new String[0];
		}

		try
		{
			switch(audioType)
			{
				case AUDIO_CLIP :
					managedAudioClips = new AudioClip[files.length];
					
					for (int i=0 ; i<files.length ; i++)
					{
						managedAudioClips[i] = applet.getGameMedia().loadAudioClip(files[i]);
					}
				break;
				case AUDIO_STREAM :
					if (sunAudio == null)
					{
						System.err.println("Unable to play sounds [sun.audio]... Sound is disabled");
						playSounds = false;
					}
					else
					{
						sunAudio.loadAudio(files);
					}
				break;
				case NO_SOUND :
					System.err.println("Unknown JVM Vendor... Sound is disabled");
					playSounds = false;
				break;
			}
		}
		catch(Exception e)
		{
			System.err.println("Error encountered while loading sound files... Sound is disabled");
			playSounds = false;
		}
	}
	
	private final int getVendorType()
	{
		String vendor = System.getProperty("java.vendor");
		
		for (int i=0 ; i<this.AUDIO_CLIP_VENDORS.length ; i++)
		{
			if (vendor.equals(AUDIO_CLIP_VENDORS[i]))
			{
				return this.AUDIO_CLIP;
			}
		}
		
		for (int i=0 ; i<this.AUDIO_STREAM_VENDORS.length ; i++)
		{
			if (vendor.equals(AUDIO_STREAM_VENDORS[i]))
			{
				return this.AUDIO_STREAM;
			}
		}
		
		return this.NO_SOUND;
	}	
	
	public final void setSoundState(boolean soundEnabled)
	{
		playSounds = soundEnabled;
	}
	
	public final boolean getSoundState()
	{
		return playSounds;
	}
	
	public final void playSound(int sound)
	{
		if (!playSounds) return;
		
		try
		{
			switch(audioType)
			{
				case AUDIO_CLIP :
					if (sound>=0 && sound<managedAudioClips.length)
					{
						managedAudioClips[sound].play();
					}
				break;
				case AUDIO_STREAM :
					if (sunAudio == null)
					{
						System.err.println("Unable to play sounds [package sun.audio]... Sound is disabled");
						playSounds = false;
					}
					else
					{
						sunAudio.playSound(sound);
					}				
				

				break;
			}
		}
		catch(Exception e)
		{
			System.err.println("Error encountered while playing a sound file... Sound is disabled");
			playSounds = false;
		}
	}
}