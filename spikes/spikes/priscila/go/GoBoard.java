package spikes.priscila.go;

import static spikes.priscila.go.GoBoard.StoneColor.BLACK;
import static spikes.priscila.go.GoBoard.StoneColor.WHITE;

import java.util.Arrays;

import wheel.io.serialization.DeepCopier;

public class GoBoard {

	public static enum StoneColor { BLACK,	WHITE; }

	public GoBoard(int size) {
		_intersections = new Intersection[size][size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				Intersection newOne = new Intersection();
				_intersections[x][y] = newOne;
				if (x != 0) newOne.connectToYourLeft(_intersections[x - 1][y]);
				if (y != 0) newOne.connectUp(_intersections[x][y - 1]);
			}
		}
	}
	
	public GoBoard(String[] setup) {
		this(setup.length);
		setup(setup);
	}

	private Intersection[][] _intersections;
	private StoneColor _nextToPlay = BLACK;
	private Intersection[][] _previousSituation;
	
	protected Intersection intersection(int x, int y) {
		return _intersections[x][y];
	}
	
	public int size() {
		return _intersections.length;
	}
	
	public boolean canPlayStone(int x, int y) {
		Intersection[][] situation = copySituation();
		try {
			tryToPlayStone(x, y);
		} catch (IllegalMove im) {
			return false;
		} finally {
			restoreSituation(situation);
		}
		
		return true;
	}

	
	private void restoreSituation(Intersection[][] situation) {
		_intersections = situation;
	}

	private Intersection[][] copySituation() {
		return (Intersection[][])DeepCopier.deepCopy(_intersections);
	}

	public void playStone(int x, int y) {
		Intersection[][] situationFound = copySituation();

		try {
			tryToPlayStone(x, y);
		} catch (IllegalMove e) {
			throw new IllegalArgumentException(e);
		}
		
		_previousSituation = situationFound;
		next();
	}

	private void tryToPlayStone(int x, int y) throws IllegalMove{
		intersection(x, y).setStone(_nextToPlay);
		
		killSurroundedStones(other(_nextToPlay));
		if (killSurroundedStones(_nextToPlay))
			throw new IllegalMove();
		
		if(sameSituationAs(_previousSituation))
			throw new IllegalMove();
	}
	
	private boolean sameSituationAs(Intersection[][] situation) {
		return Arrays.deepEquals(situation, _intersections);
	}

	private boolean killSurroundedStones(StoneColor color) {
		for(Intersection[] column: _intersections)
			for(Intersection intersection: column)
				if (intersection.killGroupIfSurrounded(color))
					return true;
		return false;
	}


	public void passTurn() {
		next();
	}

	private void next() {
		_nextToPlay = other(_nextToPlay);
	}

	public StoneColor other(StoneColor color) {
		return color == BLACK
			? WHITE
			: BLACK;
	}

	public StoneColor stoneAt(int x, int y) {
		return intersection(x, y)._stone;
	}

	public StoneColor nextToPlay() {
		return _nextToPlay;
	}
	
	private void setup(String[] setup){
		int y =0;
		for(String line: setup){
			int x=0;
			for(char position:line.toCharArray()){
				if(position == 'x'){
					intersection(x++, y)._stone = BLACK;
				} else if(position == 'o'){
					intersection(x++, y)._stone = WHITE;
				}else if(position == '+'){ 
					x++;
				}
			}
			y++;
		}
	}
	
	public String printOut(){
		StringBuffer result= new StringBuffer();
		
		for (int y = 0; y < size(); y++) {
			for (int x = 0; x < size(); x++) {
				StoneColor stone = stoneAt(x, y);
				if(stone == WHITE)
					result.append(" o");
				else if(stone == BLACK)
					result.append(" x");
				else
					result.append(" +");
			}
			result.append("\n");
		}
		return result.toString();
	}
}
