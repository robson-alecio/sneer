/*
 * [ JIGA ]
 * 
 * Copyright (c) 2003 Glenn Sanson <glenn.sanson at free.fr>
 * 
 * This code is distributed under the GNU Library General Public License
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * 
 * [http://glenn.sanson.free.fr/jiga/]
 */

package frozenbubble.game.net.library.jiga;

import sun.audio.AudioDataStream;
import sun.audio.AudioStream;
import sun.audio.AudioPlayer;

/**
 * This class provides sound management functions using the non-standard <code>sun.audio</code> package.
 * This is mostly use to play sounds with the netscape native virtual machine
 * @author Glenn Sanson
 */
public class SunAudioManager {
    private GameApplet gameApplet;

    private AudioDataStream[] managedAudioDataStreams;

    public SunAudioManager(GameApplet gameApplet1) {
        this.gameApplet = gameApplet1;
    }

    private AudioDataStream loadAudioStream(String filename) {
        AudioDataStream ads = (AudioDataStream) gameApplet.getGameContext().getObject(filename);

        if (ads != null) {
            return ads;
        }

        try {
            AudioStream as = new AudioStream(gameApplet.getClass().getResourceAsStream(filename));

            ads = new AudioDataStream(as.getData());

            gameApplet.getGameContext().addObject(filename, ads);
        } catch (Exception e) {
            System.err.println("Audio File [" + filename + "] not found");
        }

        return ads;
    }

    /**
     * Loads a set of audio files
     * @param files the name (location) of the differents audio files to load
     */
    public void loadAudio(String[] files) {
        managedAudioDataStreams = new AudioDataStream[files.length];

        for (int i = 0; i < files.length; i++) {
            managedAudioDataStreams[i] = loadAudioStream(files[i]);
        }
    }

    /**
     * Plays a sound in the list previously loaded
     * @param sound Index of the sound to play
     */
    public void playSound(int sound) {
        if (sound >= 0 && sound < managedAudioDataStreams.length) {
            managedAudioDataStreams[sound].reset();
            AudioPlayer.player.start(managedAudioDataStreams[sound]);
        }
    }
}