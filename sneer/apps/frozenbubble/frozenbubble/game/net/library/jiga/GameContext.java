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

import java.util.Vector;

/**
 * A convenient way to maintain all objects required by the game, which allows
 * data exchange between GameScreens
 * <p>FAQ : Why do I use <code>Vector</code> to simulate a <code>HashMap</code>?
 * <p>A : Because <code>HashMap</code> was implemented in Java release 1.2 and JIGA is 1.1 compatible
 * <p>Q : Why didn't I use <code>Hashtable</code> instead?
 * <p>A : Because of a bug in the implementation of <code>Hashtable</code> on some old VMs 
 * (N.B.: <code>GameContext</code> acts like a <code>HashMap</code> : 
 * <ul>
 * <li>this is not a synchronized object -<code>Vector</code> is synchronized, but not <code>GameContext</code>- 
 * <li><code>null</code> values are allowed
 * </ul> 
 * @author Glenn Sanson
 */
public class GameContext {
    @SuppressWarnings("unchecked")
	private Vector dataList;

    @SuppressWarnings("unchecked")
	private Vector keyList;

    /**
     * Initialisation of the pseudo HashMap
     *  
     */
    @SuppressWarnings("unchecked")
	public GameContext() {
        dataList = new Vector();
        keyList = new Vector();
    }

    /**
     * Adds an objet
     * @param key The key associated with the object to add
     * @param object The object to add
     */
    @SuppressWarnings("unchecked")
	public final void addObject(String key, Object object) {
        int index = keyList.indexOf(key);

        if (index == -1) {
            keyList.addElement(key);
            dataList.addElement(object);
        } else {
            dataList.setElementAt(object, index);
        }
    }

    /**
     * Removes an objet
     * @param key The key associated with the object to remove
     */
    public final void removeObject(String key) {
        int index = keyList.indexOf(key);

        if (index != -1) {
            keyList.removeElementAt(index);
            dataList.removeElementAt(index);
        }
    }

    /**
     * Gets an objet
     * @param key The key associated with the object to get
     * @return The searched object, or <code>null</code> if the key was not found
     */    
    public final Object getObject(String key) {
        int index = keyList.indexOf(key);

        if (index != -1) {
            return dataList.elementAt(index);
        }

        return null;
    }
}