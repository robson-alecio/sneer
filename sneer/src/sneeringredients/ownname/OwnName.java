package sneeringredients.ownname;

import wheel.reactive.Receiver;
import wheel.reactive.Signal;

public interface OwnName {

	public Signal<String> name();
	public Signal<QuestionForUser> question();
	
	public Receiver<UserAnswer> answerReceiver();
}
