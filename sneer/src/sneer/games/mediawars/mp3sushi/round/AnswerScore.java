/**
 * 
 */
package sneer.games.mediawars.mp3sushi.round;

public class AnswerScore {
	Long _id;
	int _score;
	
	public AnswerScore(Long id, int score) {
		super();
		_id = id;
		_score = score;
	}
	public Long getId() {
		return _id;
	}
	public int getScore() {
		return _score;
	}
	
}