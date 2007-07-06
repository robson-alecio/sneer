package sneeringredients.ownname;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface OwnName {

	public Signal<String> name();
	public Signal<QuestionForUser> question();
	
	public Omnivore<UserAnswer> answerReceiver();
}
