package spikes.lucass;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import spikes.lucass.PieceSet.ChessOptions;
import spikes.lucass.PieceSet.Game;

public class GameFrame extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int SLEEP= 30;
	
	/**
	 * O Graphics onde vai ser pintado ao inv�s do Graphics padr�o
	 */
	protected Graphics _bufferGraphics;
	
	/**
	 * A imagem q vai conter o bufferGraphics.
	 */
    private BufferedImage _offscreen;
    
    /**
     * A Thread principal do jogo.
     */
    private Thread _ticker;
    
    /**
     * Marca se a Thread principal est� rodando. 
     */
    protected boolean _running;
	
	private Game _chessGame;
	
	Image _piecesImage;
	
	public GameFrame() {
		_chessGame = new Game(new ChessOptions());
		
		addMouseListener(_chessGame);
		addMouseMotionListener(_chessGame);
		
        startThread();
        
        createBufferImage();
        
        setSize(_chessGame.getBoard().getBoardWidth(),_chessGame.getBoard().getBoardHeight());
	}

	private void createBufferImage() {
		_offscreen = new BufferedImage(_chessGame.getBoard().getBoardWidth(),_chessGame.getBoard().getBoardHeight(),BufferedImage.TYPE_INT_ARGB);
        _bufferGraphics = _offscreen.getGraphics();
        setBackground(Color.white);
	}

	private void startThread() {
		if (_ticker == null || _ticker.isAlive()){
            _running= true;
            _ticker = new Thread(this);
            _ticker.start();
        }
	}
	
    public synchronized void stop(){
        _running= false;
    }

    public void run() {
        while(_running){
            step();
            repaint();
            try{
                Thread.sleep(1000/SLEEP);
            }
            catch (InterruptedException e){}
        }
    }
    
    @Override
    public void repaint() {
    	if(getGraphics()!=null){
    		paint(_bufferGraphics);
    		getGraphics().drawImage(_offscreen,0,0,this);
    	}
    }
    
    @Override
    public void paint(Graphics g) {
    	_chessGame.paint(g);
    }
    
    @Override
	public void setBackground(Color color){
    	if(getGraphics()!=null)
    		getGraphics().setColor(color);
    	if(_bufferGraphics!=null)
    		_bufferGraphics.setColor(color);
    }
    
    /**
     * Chamada a cada frame do Main.
     *
     */
    public void step(){
    }
    
    public void destroy()
    {
    	_running= false;
        _ticker= null;
    }
}
