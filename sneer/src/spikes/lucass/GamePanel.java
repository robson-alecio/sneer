package spikes.lucass;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import spikes.lucass.GameBase.Game;
import spikes.lucass.GameBase.GameTypes.ChessOptions;
import spikes.lucass.GameBase.GameTypes.GoOptions;

public class GamePanel extends JPanel implements Runnable, FocusListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int FRAMES_PER_SECOND= 30;
	
    private BufferedImage _bufferImage;
    protected Graphics _bufferImageGraphics;
    
    /**
     * A Thread principal do jogo.
     */
    private Thread _ticker;
    
    private boolean _isThreadRunning;
    private boolean _isGamePanelFocused;
    
	private Game _game;
	
	public GamePanel() {
		setFocusable(true);
		createGame();
        createBufferImage();
        setSize(_game.getBoard().getBoardWidth(),_game.getBoard().getBoardHeight());
        startThread();
	}

	private void createGame() {
		_game = new Game(new ChessOptions());
		
		addMouseListener(_game.getMouseListener());
		addMouseMotionListener(_game.getMouseMotionListener());
		
		addFocusListener(this);
	}

	private void createBufferImage() {
		_bufferImage = new BufferedImage(_game.getBoard().getBoardWidth(),_game.getBoard().getBoardHeight(),BufferedImage.TYPE_INT_ARGB);
        _bufferImageGraphics = _bufferImage.getGraphics();
        setBackground(Color.white);
	}

	private void startThread() {
		if (_ticker == null || _ticker.isAlive()){
            _isThreadRunning= true;
            _ticker = new Thread(this);
            _ticker.start();
        }
	}
	
    public synchronized void stop(){
        _isThreadRunning= false;
    }

    public void run() {
        while(_isThreadRunning){
            step();
            if(_isGamePanelFocused)
            	repaint();
            try{
                Thread.sleep(1000/FRAMES_PER_SECOND);
            }
            catch (InterruptedException e){}
        }
    }
    
    @Override
    public void repaint() {
    	if(getGraphics()!=null){
    		paint(_bufferImageGraphics);
    		getGraphics().drawImage(_bufferImage,0,0,this);
    	}
    }
    
    @Override
    public void paint(Graphics g) {
    	_game.paint(g);
    }
    
    @Override
	public void setBackground(Color color){
    	if(getGraphics()!=null)
    		getGraphics().setColor(color);
    	if(_bufferImageGraphics!=null)
    		_bufferImageGraphics.setColor(color);
    }
    
    /**
     * Chamada a cada frame do Main.
     *
     */
    public void step(){
    }
    
    public void destroy()
    {
    	_isThreadRunning= false;
        _ticker= null;
    }

	public void focusGained(FocusEvent ignored) {
		_isGamePanelFocused= true;		
	}

	public void focusLost(FocusEvent ignored) {
		_isGamePanelFocused= false;
	}
}
