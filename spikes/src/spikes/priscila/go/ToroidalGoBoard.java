package spikes.priscila.go;


public class ToroidalGoBoard extends GoBoard {

	public ToroidalGoBoard(int size) {
		super(size);
		makeToroidal();
	}

	private void makeToroidal() {
		connectTopToBottom();
		connectLeftToRight();
	}

	public ToroidalGoBoard(String[] setup) {
		super(setup);
		makeToroidal();
	}

	private void connectTopToBottom() {
		for (int x = 0; x < size(); x++) {
			Intersection top = intersection(x, 0);
			Intersection bottom = intersection(x, size() - 1);
			top.connectUp(bottom);
		}
	}

	private void connectLeftToRight() {
		for (int y = 0; y < size(); y++) {
			Intersection left = intersection(0, y);
			Intersection right = intersection(size() - 1, y);
			left.connectToYourLeft(right);
		}
	}

	
	
}
