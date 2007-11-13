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

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyListener;

/**
 * A time-driven version of the Applet class. 
 * Applet is event-based whereas most games -and lots of non games- require a "time-step" mechanism.
 * This class should simplify basic devs 
 */
public abstract class GameApplet extends Applet implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    /** The time-step of the applet. By default, this value is set to 25 ms (= 40 frames per second) */
    protected static long REFRESH_RATE_MILLIS = 25;

    private Graphics gBack;

    private Image iBack;

    private GameScreen currentScreen;

    private GameContext currentContext;

    private GameMedia currentMedia;

    private SunAudioManager sunAudio;

    private int gameWidth, gameHeight;

    private int bufferWidth, bufferHeight;

    private boolean gameRunning, threadRunning;

    private int[] listKeys;

    private Point mousePosition;

    private boolean leftMouseButtonState;

    private boolean rightMouseButtonState;

    private Point screenPosition;

    private boolean isApplication = false;

    /**
     * Initialise current app(let). 
     * This method is automatically called by the browser at launch time.
     * When launched as application, initialisation is called by <code>AppletFrame</code> 
     * @AppletFrame
     */
    @Override
	public void init() {
        gameWidth = this.getSize().width;
        gameHeight = this.getSize().height;

        bufferWidth = gameWidth;
        bufferHeight = gameHeight;
        screenPosition = new Point(0, 0);

        iBack = this.createImage(gameWidth, gameHeight);
        gBack = iBack.getGraphics();

        currentContext = new GameContext();
        currentMedia = new GameMedia(this);

        try {
            sunAudio = new SunAudioManager(this);
        } catch (Exception e) {
            System.err.println("Package sun.audio is not available");
        }

        gameInit();

        setCurrentScreen(this.getInitialScreen());

        listKeys = new int[0];
        mousePosition = new Point(0, 0);

        if (needsKeyEvents()) {
            addKeyListener(this);
        }

        if (needsMouseEvents()) {
            addMouseListener(this);
            addMouseMotionListener(this);
        }
    }

    /**
     * Defines the double-buffer size. 
     * Double-buffer needn't have the same size as the screen,
     * and can be larger (scrolling)
     * @param newWidth The new double-buffer width
     * @param newHeight The new double-buffer height
     */
    public void setFrameBufferSize(int newWidth, int newHeight) {
        bufferWidth = newWidth;
        bufferHeight = newHeight;
        screenPosition.setLocation(0, 0);

        iBack = this.createImage(Math.max(gameWidth, newWidth), Math.max(gameHeight, newHeight));
        gBack = iBack.getGraphics();
    }

    /**
     * Defines the position of the screen within the double-buffer space
     * @param x X-Coordinate of the screen
     * @param y Y-Coordinate of the screen
     */
    public void setScreenPosition(int x, int y) {
        screenPosition.setLocation(Math.max(0, Math.min(x, bufferWidth - gameWidth)), Math.max(0, Math.min(y, bufferHeight - gameHeight)));
    }

    /**
     * Retrieves the current screen position in the double-buffer space
     * @return The position of the upper left corner of the screen
     */
    public Point getScreenPosition() {
        return new Point(screenPosition);
    }

    /** 
     * Defines the current applet as application.
     * This function should not be called directly
     */
    public void setAsApplication() {
        this.isApplication = true;
    }

    /**
     * Checks whether the current applet is an application
     * @return <code>true</code> only if the game was launched as application
     */
    public boolean isApplication() {
        return this.isApplication;
    }

    /**
     * Retrieves the current sound manager object. 
     * @return the audio manager associated with the current processus
     * @see SunAudioManager
     */
    public final SunAudioManager getSunAudioManager() {
        return sunAudio;
    }

    /**
     * Defines whether this applet needs key data or not 
     * @return <code>true</code> if this feature is required
     */
    public boolean needsKeyEvents() {
        return true;
    }

    /**
     * Defines whether this applet needs key data or not.
     * N.B. : This consume a lot of CPU 
     * @return <code>true</code> if this feature is required
     */    
    public boolean needsMouseEvents() {
        return true;
    }

    /**
     * Returns applet width (NOT buffer width)
     * @return Appl(let) width
     */
    @Override
	public final int getWidth() {
        return gameWidth;
    }

    /**
     * Returns applet height (NOT buffer height)
     * @return Appl(let) width
     */    
    @Override
	public final int getHeight() {
        return gameHeight;
    }

    /**
     * Returns buffer width
     * @return Current buffer width
     */
    public final int getBufferWidth() {
        return bufferWidth;
    }

    /**
     * Returns buffer height
     * @return Current buffer height
     */
    public final int getBufferHeight() {
        return bufferHeight;
    }

    public final Image getBackgroundImage() {
        return iBack;
    }

    public final Graphics getBackgroundGraphics() {
        return gBack;
    }

    public final GameContext getGameContext() {
        return currentContext;
    }

    public final GameMedia getGameMedia() {
        return currentMedia;
    }

    public abstract GameScreen getInitialScreen();

    public void gameInit() {
    };

    public final void setCurrentScreen(GameScreen newCurrentScreen) {
        currentScreen = newCurrentScreen;
        currentScreen.initBackground();
    }

    @Override
	public final void start() {
        while (threadRunning)
            ;

        gameRunning = true;

        new Thread(this).start();
    }

    @Override
	public final void stop() {
        gameRunning = false;
    }

    public final void run() {
        long time;

        threadRunning = true;

        while (gameRunning) {
            time = System.currentTimeMillis();

            currentScreen.play(listKeys, mousePosition, leftMouseButtonState, rightMouseButtonState);
            currentScreen.refreshAll();
            currentScreen.paintAll();

            time = System.currentTimeMillis() - time;

            if (time < REFRESH_RATE_MILLIS) {
                try {
                    Thread.sleep(REFRESH_RATE_MILLIS - time);
                } catch (Exception e) {
                    System.err.println("[Thread] - Unable to sleep");
                }
            }

            if (gameRunning) {
                paint(this.getGraphics());
            }
        }

        threadRunning = false;
    }

    @Override
	public final void paint(Graphics g) {
        if (iBack != null) {
            g.drawImage(iBack, -screenPosition.x, -screenPosition.y, this);
        }
    }

    public final void keyPressed(KeyEvent e) {
        int currentKey = e.getKeyCode();

        if (currentKey == 18) {
            return;
        }

        if (listKeys.length != 0) {
            if (listKeys[0] != currentKey) {
                int[] newListKeys = new int[listKeys.length + 1];

                newListKeys[0] = currentKey;
                for (int i = 0; i < listKeys.length; i++) {
                    newListKeys[i + 1] = listKeys[i];
                }

                listKeys = newListKeys;
            }
        } else {
            listKeys = new int[1];
            listKeys[0] = currentKey;
        }
    }

    public final void keyReleased(KeyEvent e) {
        int currentKey = e.getKeyCode();

        if (listKeys.length != 0) {
            int[] newListKeys = new int[listKeys.length - 1];
            boolean found = false;

            for (int i = 0; i < listKeys.length; i++) {
                if (found) {
                    newListKeys[i - 1] = listKeys[i];
                } else {
                    if (i != listKeys.length - 1) {
                        newListKeys[i] = listKeys[i];
                    }
                }

                if (listKeys[i] == currentKey) {
                    found = true;
                }

                if (i == listKeys.length - 1 && !found) {
                    return;
                }
            }

            listKeys = newListKeys;
        }
    }

    public final void keyTyped(KeyEvent e) {
    }

    public final void mouseClicked(MouseEvent e) {
    }

    public final void mouseEntered(MouseEvent e) {
        mousePosition = e.getPoint();

        paint(this.getGraphics());
    }

    public final void mouseExited(MouseEvent e) {
        mousePosition = e.getPoint();

        paint(this.getGraphics());
    }

    public final void mousePressed(MouseEvent e) {
        mousePosition = e.getPoint();

        int buttons = e.getModifiers();

        if ((buttons & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
            leftMouseButtonState = !leftMouseButtonState;
        }

        if ((buttons & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
            rightMouseButtonState = !rightMouseButtonState;
        }

        paint(this.getGraphics());
    }

    public final void mouseReleased(MouseEvent e) {
        mousePosition = e.getPoint();

        int buttons = e.getModifiers();

        if ((buttons & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
            leftMouseButtonState = !leftMouseButtonState;
        }

        if ((buttons & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
            rightMouseButtonState = !rightMouseButtonState;
        }

        paint(this.getGraphics());
    }

    public final void mouseDragged(MouseEvent e) {
        mousePosition = e.getPoint();

        paint(this.getGraphics());
    }

    public final void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();

        paint(this.getGraphics());
    }
}