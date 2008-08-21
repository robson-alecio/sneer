package spikes.lucass.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import spikes.lucass.game.base.Game;
import spikes.lucass.game.type.GameOptions;

public class GamePanel extends JPanel implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int FRAMES_PER_SECOND= 15;
	
    private BufferedImage _bufferImage;
    private Graphics _bufferImageGraphics;
    
    /**
     * A Thread principal do jogo.
     */
    private Thread _ticker;
    
    private boolean _isThreadRunning;
    private boolean _isGamePanelFocused;
    
	private Game _game;
	
	public GamePanel(GameOptions gameType) {
		setFocusable(true);
		setGame(gameType);
		startThread();
	}

	public void setGame(GameOptions gameType) {
		createGame(gameType);
        createBufferImage();
        setSize(_game.getBoard().getBoardWidth(),_game.getBoard().getBoardHeight());
        setContextOptions(gameType);
	}

	private void setContextOptions(GameOptions gameType) {
		JPopupMenu contextOptions= new JPopupMenu();
		
		JMenu addPieces= new JMenu(Resources.getString(Resources.ADD_PIECE));
		
		String[] pieces= gameType.getPieceList();
		for(int i=0; i<pieces.length; i++){
			JMenuItem newPieceName= new JMenuItem(pieces[i]);
			
			newPieceName.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					System.out.println(((JMenuItem)e.getSource()).getText());
				}
			});
			
			addPieces.add(newPieceName);
		}
		
		contextOptions.add(addPieces);
		
		contextOptions.add(new JMenuItem(Resources.getString(Resources.DELETE_PIECE)));
		
        this.setComponentPopupMenu(contextOptions);
	}

	private void createGame(GameOptions gameType) {
		_game = new Game(gameType);
		
		addMouseListener(_game.getMouseListener());
		addMouseMotionListener(_game.getMouseMotionListener());
		addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent ignored) {
				_isGamePanelFocused= true;		
			}
			public void focusLost(FocusEvent ignored) {
				_isGamePanelFocused= false;
			}
		});
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
}
