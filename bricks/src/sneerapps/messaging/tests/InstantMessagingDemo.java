package sneerapps.messaging.tests;

import sneerapps.messaging.InstantMessagingFrame;
import wheel.lang.FrozenTime;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class InstantMessagingDemo {

	public static void main(String[] args) {
		Signal<String> nick = new RegisterImpl<String>("Ana").output();
		final Register<String> in = new RegisterImpl<String>(null);
		Omnivore<String> out = new Omnivore<String>(){ @Override public void consume(String message) {
			in.setter().consume(message);
		}};
		
		FrozenTime.freezeForCurrentThread(System.currentTimeMillis());
		new InstantMessagingFrame(nick , in.output(), out, null);
		
	}

}
