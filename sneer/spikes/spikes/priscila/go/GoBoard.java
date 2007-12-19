package spikes.priscila.go;

import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public class GoBoard {

	static protected class Intersection {

		private Intersection _left;
		private Intersection _right;
		private Intersection _up;
		private Intersection _down;
		
		private Color _stone = null;

		protected void connectToYourLeft(Intersection other) {
			_left = other;
			other._right = this;
		}

		protected void connectUp(Intersection other) {
			_up = other;
			other._down = this;
		}

		private boolean isEmpty() {
			return _stone == null;
		}

		private void setStone(Color stoneColor) {
			if (!isEmpty()) throw new IllegalStateException();
			_stone = stoneColor;
		}


	}

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
	
	private final Intersection[][] _intersections;
	private Color _nextToPlay = BLACK;

	protected Intersection intersection(int x, int y) {
		return _intersections[x][y];
	}
	
	public int size() {
		return _intersections.length;
	}
	
	public boolean canPlayStone(int x, int y) {
		return intersection(x, y).isEmpty();
	}
	
	public void playStone(int x, int y) {
		intersection(x, y).setStone(_nextToPlay);
		
		killSurroundedStones(other(_nextToPlay));
		killSurroundedStones(_nextToPlay);
		
		next();
	}

	private void killSurroundedStones(Color color) {
		for(Intersection[] column: _intersections)
			for(Intersection intersection: column)
				killIfSurrounded(intersection, color);
	}

	private void killIfSurrounded(Intersection intersection, Color color) {
		if (intersection._stone == null) return;
		if (intersection._stone != color) return;
		
		Color other = other(intersection._stone);
		if(intersection._down._stone == other 
			&& intersection._up._stone == other
			&& intersection._left._stone == other
			&& intersection._right._stone == other)
				intersection._stone = null;

	}

	public void passTurn() {
		next();
	}

	private void next() {
		_nextToPlay = other(_nextToPlay);
	}

	public Color other(Color color) {
		return color == BLACK
			? WHITE
			: BLACK;
	}

	public Color stoneAt(int x, int y) {
		return intersection(x, y)._stone;
	}

	public Color nextToPlay() {
		return _nextToPlay;
	}
}
