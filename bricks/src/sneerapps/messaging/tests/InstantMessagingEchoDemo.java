package sneerapps.messaging.tests;

import javax.swing.SwingUtilities;

import sneerapps.messaging.InstantMessagingFrame;
import wheel.lang.FrozenTime;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

public class InstantMessagingEchoDemo {

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {@Override public void run() {
			FrozenTime.freezeForCurrentThread(System.currentTimeMillis());
		}});
		
		openFrame();
	}

	private static void openFrame() {
		Signal<String> nick = new RegisterImpl<String>("Ana").output();
		final Register<String> lastReceivedMessage = new RegisterImpl<String>(null);
		Omnivore<String> messageSender = new Omnivore<String>(){ @Override public void consume(String messageToSend) {
			lastReceivedMessage.setter().consume(messageToSend);
		}};
		
		new InstantMessagingFrame(nick , lastReceivedMessage.output(), messageSender, null);
	}

}
