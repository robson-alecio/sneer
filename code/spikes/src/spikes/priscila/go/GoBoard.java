package spikes.priscila.go;

import static sneer.foundation.environments.Environments.my;
import static spikes.priscila.go.GoBoard.StoneColor.BLACK;
import static spikes.priscila.go.GoBoard.StoneColor.WHITE;

import java.util.Arrays;

import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.serialization.DeepCopier;

public class GoBoard {

	public static enum StoneColor { BLACK,	WHITE; }
	private final Register<Integer> _blackStonesCaptured = my(Signals.class).newRegister(0);
	private final Register<Integer> _whiteStonesCaptured = my(Signals.class).newRegister(0);
	
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
		return my(DeepCopier.class).deepCopy(_intersections);
	}

	public void playStone(int x, int y) {
		Intersection[][] situationFound = copySituation();

		try {
			tryToPlayStone(x, y);
		} catch (IllegalMove e) {
			throw new IllegalArgumentException(e);
		}
		
		_previousSituation = situationFound;
		countCapturedStones();
		next();
	}

	private void countCapturedStones() {
		countCapturedStones(_blackStonesCaptured, BLACK);
		countCapturedStones(_whiteStonesCaptured, WHITE);
	}

	private void countCapturedStones(Register<Integer> counter,	StoneColor color) {
		Integer captured = countCapturedStones(color);
		counter.setter().consume(counter.output().currentValue() + captured);
	}

	private int countCapturedStones(StoneColor color) {
		int result = 0;
		
		for (int line = 0; line < size(); line++)
			for (int column = 0; column < size(); column++) {
				StoneColor previous = _previousSituation[line][column]._stone;
				StoneColor current = _intersections[line][column]._stone;
				if (previous == color && current == null) result++;
			};
		
		return result;
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
		boolean wereStonesKilled = false;
		for(Intersection[] column : _intersections)
			for(Intersection intersection : column)
				if (intersection.killGroupIfSurrounded(color))
					wereStonesKilled = true;
					
		return wereStonesKilled;
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
		for (int y = 0; y < setup.length; y++)
			setupLine(y, setup[y]);
	}

	private void setupLine(int y, String line) {
		int x = 0;
		for(char symbol : line.toCharArray()) {
			if (symbol == ' ') continue;
			
			StoneColor stone = null;
			if(symbol == 'x') stone = BLACK;
			if(symbol == 'o') stone = WHITE;
			
			intersection(x, y)._stone = stone;
			x++;
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
	
	public Signal<Integer> blackCapturedCount() {
		return _blackStonesCaptured.output();
	}

	public Signal<Integer> whiteCapturedCount() {
		return _whiteStonesCaptured.output();
	}
}
