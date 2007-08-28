package sneer.kernel.pointofview.impl;

import java.util.Date;
import java.util.Random;

import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.graphics.JpgImage;
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
	
	private final Source<String> _thoughtOfTheDay = new SourceImpl<String>("thoughtOfTheDay");
	private final Source<JpgImage> _picture = new SourceImpl<JpgImage>(null);
	private final Source<String> _profile = new SourceImpl<String>("profile");

	private volatile boolean _isCrashed = false;

	
	public FakeParty(String namePrefix) {
		_namePrefix = namePrefix;
		Threads.startDaemon(randomizer());
	}

	private Runnable randomizer() {
		return new Runnable() { @Override public void run() {
			while (!_isCrashed) {
				Threads.sleepWithoutInterruptions(2000 + RANDOM.nextInt(1000));
				randomize();
			}
		}};
	}
	
	private void randomize() {
		randomizeContacts();
		
		_host.setter().consume("host " + new Date());

		int port = RANDOM.nextInt(100);
		if (port !=_port.output().currentValue())
			_port.setter().consume(port);

		boolean isOnline = RANDOM.nextBoolean();
		if (isOnline !=_isOnline.output().currentValue())
			_isOnline.setter().consume(isOnline);
	
		boolean publicKeyConfirmed = RANDOM.nextBoolean();
		if (publicKeyConfirmed!=_publicKeyConfirmed.output().currentValue())
			_publicKeyConfirmed.setter().consume(publicKeyConfirmed);
	}

	private void randomizeContacts() {
		int index = RANDOM.nextInt(2);
		
		if (index < _fakeContacts.output().currentSize())
			removeFake(index);
		else
			_fakeContacts.add(new FakeContact(_namePrefix + " " + index));
		
		randomizeOneContact();
	}

	private void removeFake(int index) {
		FakeContact fake = (FakeContact)_fakeContacts.output().currentGet(index);
		((FakeParty)fake.party()).crash();
		_fakeContacts.remove(index);
	}

	
	private void crash() {
		_isCrashed  = true;
	}

	private void randomizeOneContact() {
		int size = _fakeContacts.output().currentSize();
		if (size == 0) return;
		
		int index = RANDOM.nextInt(size);
		FakeContact fakeContact = (FakeContact)_fakeContacts.output().currentGet(index);
		fakeContact.randomize();
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
		return new SourceImpl<String>(_namePrefix).output(); //Fix:have no idea if this is correct!
	}

	@Override
	public Signal<Integer> port() {
		return _port.output();
	}

	@Override
	public Signal<Boolean> publicKeyConfirmed() {
		return _publicKeyConfirmed.output();
	}
	
	@Override
	public String toString(){
    	return _namePrefix;//Fix:have no idea if this is correct!
    }

	@Override
	public Contact currentContact(String nick) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public Signal<String> publicKey() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	public Signal<JpgImage> picture() {
		return _picture.output();
	}

	public Signal<String> profile() {
		return _profile.output();
	}

	public Signal<String> thoughtOfTheDay() {
		return _thoughtOfTheDay.output();
	}


}
