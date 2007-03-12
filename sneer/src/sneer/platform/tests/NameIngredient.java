package sneer.platform.tests;

import wheel.reactive.Receiver;
import wheel.reactive.Signal;

public interface NameIngredient {

	public Signal<String> name();
	public Signal<QuestionForUser> question();
	
	public Receiver<UserAnswer> answerReceiver();
}
