package sneer.games.mediawars.mp3sushi.player;

import wheel.reactive.Signal;
import wheel.reactive.impl.SourceImpl;

public abstract class PlayerIdentification {

	private SourceImpl<Integer> totalScore = new SourceImpl<Integer>(new Integer(0));
	private SourceImpl<Integer> actualScore = new SourceImpl<Integer>(null);;
	
	public SourceImpl<Integer> getTotalScore() {
		return totalScore;
	}

	public SourceImpl<Integer> getActualScore() {
		return actualScore;
	}

	public void resetActualScore() {
		setActualScore(null);
	}
	
	public void setActualScore(Integer score) {
		if (actualScore.output().currentValue() == null) {
			if (score == null) return; 
			actualScore.setter().consume(score);
		} else	if (!actualScore.output().currentValue().equals(score)) {
			actualScore.setter().consume(score);
		}
		if (score != null && (!score.equals(new Integer(0)))) {
			totalScore.setter().consume(score + totalScore.output().currentValue());
		}
	}

	public abstract Signal<String> getNick();
	public abstract Long getId();
}
