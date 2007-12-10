package sneer.kernel.pointofview.impl.tests;

import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.business.impl.BusinessFactory;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import sneer.kernel.pointofview.impl.Me;
import sneer.kernel.pointofview.tests.PartySimulator;
import wheel.io.network.OldNetwork;
import wheel.io.ui.User;
import wheel.io.ui.impl.UserAdapter;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.impl.SourceImpl;

class PartySimulatorImpl implements PartySimulator {

	private final Party _pointOfView;
	private final BusinessSource _businessSource;

	public PartySimulatorImpl(String name, OldNetwork network, int port) {
		_businessSource = new BusinessFactory().createBusinessSource();
		
		try {
			_businessSource.sneerPortSetter().consume(port);
		} catch (IllegalParameter e) {
			throw new IllegalArgumentException(e);
		}
		
		Communicator communicator = new Communicator(user(), network, _businessSource);
		Channel channel = communicator.openChannel("Simulator Channel", 1);
		_pointOfView = new Me(_businessSource.output(), communicator.operator(), channel, new SourceImpl<Boolean>(false).output(), new SourceImpl<Pair<String, Boolean>>(null).output());

		nameSetter().consume(name);
	}

	private User user() {
		return new UserAdapter() {

			@Override
			public String answer(String prompt, String defaultAnswer) {
				return defaultAnswer;
			}
			
			@Override
			public boolean confirm(String proposition) {
				return true;
			}
		};

	}

	@Override
	public Contact contact(String nick) {
		return _pointOfView.currentContact(nick);
	}

	@Override
	public Omnivore<String> nameSetter() {
		return _businessSource.ownNameSetter();
	}

	void connectTo(String host, Integer port, String nick) {
		try {
			_businessSource.contactManager().contactAdder().consume(new ContactInfo(nick, host, port, ""));
		} catch (IllegalParameter e) {
			throw new IllegalArgumentException(e);
		}
	}

	String name() {
		return _businessSource.output().ownName().currentValue();
	}


}
