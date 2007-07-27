package sneer.kernel.pointofview.impl;

import java.util.Date;
import java.util.Random;

import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.lang.Threads;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class FakeParty implements Party {

	private static final Random RANDOM = new Random();
	
	private final String _namePrefix;

	private final ListSource<Contact> _fakeContacts = new ListSourceImpl<Contact>();
	private final Source<String> _host = new SourceImpl<String>("host");
	private final Source<Integer> _port = new SourceImpl<Integer>(0);
	private final Source<Boolean> _isOnline = new SourceImpl<Boolean>(false);
	private final Source<Boolean> _publicKeyConfirmed = new SourceImpl<Boolean>(false);

	
	public FakeParty(String namePrefix) {
		_namePrefix = namePrefix;
		Threads.startDaemon(randomizer());
	}

	private Runnable randomizer() {
		return new Runnable() { @Override public void run() {
			while (true) {
				Threads.waitWithoutInterruptions(2000 + RANDOM.nextInt(1000));
				randomize();
			}
		}};
	}
	
	private void randomize() {
		randomizeContacts();
		_host.setter().consume("host " + new Date());
		_port.setter().consume(RANDOM.nextInt(100));
		_isOnline.setter().consume(RANDOM.nextBoolean());
		_publicKeyConfirmed.setter().consume(RANDOM.nextBoolean());
	}

	private void randomizeContacts() {
		int index = RANDOM.nextInt(4);
		
		if (index < _fakeContacts.output().currentSize())
			_fakeContacts.remove(index);
		else
			_fakeContacts.add(new FakeContact(_namePrefix));
	}

	@Override
	public ListSignal<Contact> contacts() {
		return _fakeContacts.output();
	}

	@Override
	public Signal<String> host() {
		return _host.output();
	}

	@Override
	public Signal<Boolean> isOnline() {
		return _isOnline.output();
	}

	@Override
	public Signal<String> name() {
		return null;
	}

	@Override
	public Signal<Integer> port() {
		return _port.output();
	}

	@Override
	public Signal<Boolean> publicKeyConfirmed() {
		return _publicKeyConfirmed.output();
	}

}
