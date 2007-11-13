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

/*
 * InvalidTreeException.java
 */

public class InvalidConstructionException extends Exception {
    
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of InvalidConstructionException */
    public InvalidConstructionException() {
        super("Invalid tree construction");
    }
    
    /** 
     * Creates a new instance of InvalidConstructionException 
     * @param message The message associated with this <code>Exception</code>
     */
    public InvalidConstructionException(String message) {
        super(message);
    }
    
}
