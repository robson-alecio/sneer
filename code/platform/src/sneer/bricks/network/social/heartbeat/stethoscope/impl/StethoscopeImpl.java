package sneer.bricks.network.social.heartbeat.stethoscope.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.threads.Stepper;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMap;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMaps;
import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.heartbeat.HeartBeat;
import sneer.bricks.network.social.heartbeat.stethoscope.Stethoscope;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Producer;

class StethoscopeImpl implements Stethoscope, Consumer<HeartBeat>, Stepper {

	private static final int TIME_TO_DIE = 60 * 1000;
	private static final int MAX_BEAT_AGE = 10 * 60 * 1000;
	private static final Contact[] CONTACT_ARRAY_TYPE = new Contact[0];
	
	private CacheMap<Contact, Long> _lastBeatTimesByContact = my(CacheMaps.class).newInstance();
	private CacheMap<Contact, Register<Boolean>> _registersByContact = my(CacheMaps.class).newInstance();
	
	{
		System.out.println("STETHOSCOPE SUBSCRIBING");
		my(TupleSpace.class).addSubscription(HeartBeat.class, this);
		
		my(Clock.class).wakeUpEvery(TIME_TO_DIE, this);
	}
	
	
	@Override
	public Signal<Boolean> isAlive(Contact contact) {
		return isAliveRegister(contact).output();
	}


	private void notifyDeathOfStaleContacts() {
		long now = now();
		for (Contact contact : _registersByContact.keySet().toArray(CONTACT_ARRAY_TYPE))
			if (now - lastBeatTime(contact) > TIME_TO_DIE)
				isAliveRegister(contact).setter().consume(false);
	}


	private Register<Boolean> isAliveRegister(Contact contact) {
		return _registersByContact.get(contact, new Producer<Register<Boolean>>() { @Override public Register<Boolean> produce() {
			return my(Signals.class).newRegister(false);
		}});
	}


	private Long lastBeatTime(Contact contact) {
		return _lastBeatTimesByContact.get(contact, new Producer<Long>() { @Override public Long produce() {
			return 0L;
		}});
	}


	private boolean isTooOld(HeartBeat beat) {
		return now() - beat.publicationTime() > MAX_BEAT_AGE;
	}


	private long now() {
		return my(Clock.class).time();
	}



	private Contact contact(HeartBeat beat) {
		return my(Seals.class).contactGiven(beat.publisher());
	}


	@Override
	public void consume(HeartBeat beat) {
		System.out.println("STETHOSCOPE CONSUMING " + contact(beat).nickname().currentValue() + "    " + Thread.currentThread());

		if (my(Seals.class).ownSeal().equals(beat.publisher())) return;
		if (isTooOld(beat)) return;
		
		Contact contact = contact(beat);
		if (beat.publicationTime() < lastBeatTime(contact)) return;
		_lastBeatTimesByContact.put(contact, beat.publicationTime());

		System.out.println("ALIVE!!! " + contact);
		isAliveRegister(contact).setter().consume(true);
	}


	@Override
	public boolean step() {
		notifyDeathOfStaleContacts();
		return true;
	}

}
