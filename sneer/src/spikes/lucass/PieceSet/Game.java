package spikes.lucass.PieceSet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

public class Game implements MouseListener, MouseMotionListener{	
	private GameOptions _gameOptions;
	
	public static final int EMPTY= -1;
	
	private Board _board;
	private int[][] _boardModel;
	private int[][] _tmpBoardModel;
	
	private Vector<PieceSprite> _pieces;//TODO: poderia ser uma array?
	private PieceSprite _movingPiece;
	
	private int _pieceWidth;
	private int _pieceHeight;
	
//	private void debug_printSaveState() {
//		for(int i=0; i<_boardModel.length; i++){
//			for(int j=0; j<_boardModel[i].length; j++){
//				System.out.print(" - " + _boardModel[i][j]);
//			}
//			System.out.println();
//		}
//	}

	public Game(GameOptions gameOptions){
		_gameOptions= gameOptions;
		_boardModel= new int[gameOptions.getRowNumber()][gameOptions.getColNumber()];
		_tmpBoardModel= new int[gameOptions.getRowNumber()][gameOptions.getColNumber()];
		//cria board
		_board= new Board(gameOptions.getBoardImage());
		_board.generateBoard(gameOptions.getColNumber(), gameOptions.getRowNumber(), gameOptions.getBoardCellVariation());
		
		_pieceWidth= gameOptions.getPiecesImage().getWidth(null)/_gameOptions.getPieceTypesNumber();
		_pieceHeight= gameOptions.getPiecesImage().getHeight(null);
		
		createAndArrangePieces(gameOptions.getDefaultPositions());
	}
	
	public void saveBoardState(){		
		for(int i=0; i<_boardModel.length; i++){
			System.arraycopy(_boardModel[i], 0, _tmpBoardModel[i], 0, _boardModel[i].length);
		}
	}

	public void loadBoardState(/*BoardState bs*/){
		createAndArrangePieces(_tmpBoardModel);
	}
	
	public Board getBoard(){
		return _board;
	}
	
	public Vector<PieceSprite> getPiece(){
		return _pieces;
	}
	
	public void paint(Graphics g){
		_board.paint(g);
		for(int i = _pieces.size() -1 ; i>=0; i--){
			_pieces.elementAt(i).paint(g);
		}		
	}
	
	private void createAndArrangePieces(int[][] positions){
		clearBoard();
		
		for(int i=0; i<positions.length; i++){
			for(int j=0; j<positions[i].length;j++){
				addPieceOn(positions[i][j], i, j);
			}
		}
	}

	private void clearBoard() {
		if(_pieces==null)
			_pieces= new Vector<PieceSprite>();
		_pieces.clear();
		
		for(int i=0; i<_boardModel.length; i++){
			for(int j=0; j<_boardModel[i].length; j++){
				_boardModel[i][j]= EMPTY;
			}
		}
	}

	private void addPieceOn(int pieceId, int row, int col) {
		_boardModel[row][col]= pieceId;
		
		if(pieceId >=0){
			PieceSprite p= new PieceSprite(_gameOptions.getPiecesImage(),_pieceWidth,_pieceHeight,pieceId);
			
			p.setX( (col*_board.getCellBoardWidth()) );
			p.setY( (row*_board.getCellBoardHeight()) );
			wrapPiece(p);
			_pieces.add(p);
		}
	}
	
	private void wrapPiece(PieceSprite piece){
		//dar wrap na piece
		int pieceX= piece.getX()- _board.getX();
		int pieceY= piece.getY()- _board.getY();
		
		int col= (pieceX/_board.getCellBoardWidth());
		int row= (pieceY/_board.getCellBoardHeight()); 
		
		piece.setX( (col*_board.getCellBoardWidth()) + _board.getX() + (_board.getCellBoardWidth()/2));
		piece.setY( (row*_board.getCellBoardHeight()) + _board.getY() + (_board.getCellBoardHeight()/2));		
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON1){
			saveBoardState();
		}else{
			loadBoardState();
		}
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mousePressed(MouseEvent e) {
		for(int i= 0; i <_pieces.size() ; i++){
			if(_pieces.elementAt(i).getCollisionRectangle().contains(e.getX(), e.getY())){
				_movingPiece= _pieces.elementAt(i);
				setTileEmptyAt(_pieces.elementAt(i).getX(),_pieces.elementAt(i).getY());
				break;
			}
		}
	}

	private void setTileEmptyAt(int x, int y) {
		int pieceX= x- _board.getX();
		int pieceY= y- _board.getY();
		
		int col= (pieceX/_board.getCellBoardWidth());
		int row= (pieceY/_board.getCellBoardHeight()); 
		
		_boardModel[row][col]= EMPTY;
	}
	
	private void insertPieceAt(int x, int y,int id) {
		int pieceX= x- _board.getX();
		int pieceY= y- _board.getY();
		
		int col= (pieceX/_board.getCellBoardWidth());
		int row= (pieceY/_board.getCellBoardHeight()); 
		
		_boardModel[row][col]= id;
	}

	public void mouseReleased(MouseEvent e) {
		if(_movingPiece!=null){
			_movingPiece.setPosition(e.getX(), e.getY());
			wrapPiece(_movingPiece);
			insertPieceAt(_movingPiece.getX(), _movingPiece.getY(), _movingPiece.getPieceIndex());//bad code?
			_movingPiece= null;
		}
	}

	public void mouseDragged(MouseEvent e) {
		if(_movingPiece!=null){
			_movingPiece.setPosition(e.getX(), e.getY());
		}
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}
