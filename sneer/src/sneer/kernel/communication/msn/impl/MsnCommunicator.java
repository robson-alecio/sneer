package sneer.kernel.communication.msn.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import junit.framework.Test;
import net.sf.jml.Email;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.MsnUserStatus;
import net.sf.jml.event.MsnContactListAdapter;
import net.sf.jml.event.MsnMessageAdapter;
import net.sf.jml.event.MsnMessageListener;
import net.sf.jml.event.MsnMessengerListener;
import net.sf.jml.event.MsnSwitchboardListener;
import net.sf.jml.impl.MsnMessengerFactory;
import net.sf.jml.message.MsnInstantMessage;
import wheel.io.Log;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;

public class MsnCommunicator {

	private final Omnivore<Pair<String, Boolean>> _contactStatusSetter;

	public class MyContactListListener extends MsnContactListAdapter {

		@Override
		public void contactStatusChanged(MsnMessenger ignored, MsnContact contact) {
			boolean online = contact.getStatus() != MsnUserStatus.OFFLINE;
			Pair<String, Boolean> change = new Pair<String, Boolean>(contact.getEmail().getEmailAddress(), online);
			_contactStatusSetter.consume(change);
			
			
			
			if (online)
				ignored.sendText(Email.parseStr(contact.getEmail().getEmailAddress()), "Ôp");
//			ignored.sendText(new Email(contact.getEmail().getEmailAddress()), "Ôp");
	}

	}

	public class MyMessengerListener implements MsnMessengerListener {

		@Override
		public void exceptionCaught(MsnMessenger messenger, Throwable throwable) {
			Log.log(throwable);
		}

		@Override
		public void loginCompleted(MsnMessenger messenger) {
			_isOnline.setter().consume(true);
		}

		@Override
		public void logout(MsnMessenger messenger) {
			_isOnline.setter().consume(false);
		}

	}


	private final User _user;
	private final Source<Boolean> _isOnline = new SourceImpl<Boolean>(false);

	public MsnCommunicator(User user, Signal<String> msnAddress, Omnivore<Pair<String, Boolean>> contactStatusSetter) {
		_user = user;
		_contactStatusSetter = contactStatusSetter;
		msnAddress.addReceiver(msnAddressReceiver());
	}

	private Omnivore<String> msnAddressReceiver() {
		return new Omnivore<String>(){ @Override public void consume(String newAddress) {
			connectTo(newAddress);
		}};

	}

	private void connectTo(String newAddress) {
		if (newAddress == null || newAddress.isEmpty()) return;
			
		String password = null;
		try {
			password = _user.answer("Password for MSN address " + newAddress);
		} catch (CancelledByUser e) {
			_user.acknowledgeNotification("Restart Sneer or edit MSN Address field to reconnect to MSN.");
			return;
		}

		MsnMessenger messenger = MsnMessengerFactory.createMsnMessenger(newAddress, password);

		messenger.addContactListListener(new MyContactListListener());
		messenger.addMessageListener((MsnMessageListener) Proxy.newProxyInstance(Test.class.getClassLoader(), new Class[]{MsnMessageListener.class}, new MyHandler("---------message")));
		messenger.addSwitchboardListener((MsnSwitchboardListener) Proxy.newProxyInstance(Test.class.getClassLoader(), new Class[]{MsnSwitchboardListener.class}, new MyHandler("---------switchboard")));
		messenger.addMessengerListener(new MyMessengerListener());
		
		messenger.addMessageListener(new MsnMessageAdapter() {
			@Override
			public void instantMessageReceived(MsnSwitchboard arg0,
					MsnInstantMessage arg1, MsnContact arg2) {
				String msg = arg1.getContent();
				
				String reply;
				if (msg.indexOf("klaus") != -1) {
					reply = "lag.";
				} else if (msg.endsWith("?")) {
					reply = "dude?";
				} else if (msg.endsWith("!")){
					reply = "dude!!";
				} else if (msg.endsWith(".")) {
					reply = "right on";
				} else {
					return;
				}
				
				arg0.sendText(reply);
				
			}
		});

		messenger.login();
	};

	private static final class MyHandler implements InvocationHandler {
		private final String _id;

		public MyHandler(String id) {
			_id = id;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			String a = "";
			for(Object o : args) {
				if (a.length() > 0) a += ", ";
				
				if (o instanceof MsnInstantMessage) {
					MsnInstantMessage m = (MsnInstantMessage) o;
					a += m.getContent();
				} else {
					a += ""+o;
				}
			}
			log(_id+": " + method.getName()+"("+a+")");
			return null;
		}
	}


	private static void log(String string) {
		System.out.println(string);
	}

	public Signal<Boolean> isOnline() {
		return _isOnline.output();
	}


}
