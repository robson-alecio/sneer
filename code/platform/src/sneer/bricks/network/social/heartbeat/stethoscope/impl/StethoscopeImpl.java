package sneer.bricks.network.social.heartbeat.stethoscope.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMap;
import sneer.bricks.hardware.ram.maps.cachemaps.CacheMaps;
import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.heartbeat.Heartbeat;
import sneer.bricks.network.social.heartbeat.stethoscope.Stethoscope;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Producer;

class StethoscopeImpl implements Stethoscope, Consumer<Heartbeat>, Steppable {

	private static final int TIME_TILL_DEATH = 30 * 1000;
	private static final int MAX_BEAT_AGE = 10 * 60 * 1000;
	private static final Contact[] CONTACT_ARRAY_TYPE = new Contact[0];
	
	private CacheMap<Contact, Long> _lastBeatTimesByContact = my(CacheMaps.class).newInstance();
	private CacheMap<Contact, Register<Boolean>> _registersByContact = my(CacheMaps.class).newInstance();
	
	{
		my(TupleSpace.class).addSubscription(Heartbeat.class, this);
		my(Clock.class).wakeUpEvery(TIME_TILL_DEATH, this);
	}
	
	
	@Override
	public Signal<Boolean> isAlive(Contact contact) {
		return isAliveRegister(contact).output();
	}


	private void notifyDeathOfStaleContacts() {
		long now = now();
		for (Contact contact : _registersByContact.keySet().toArray(CONTACT_ARRAY_TYPE))
			if (now - lastBeatTime(contact) > TIME_TILL_DEATH)
				setDead(contact);
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


	private boolean isTooOld(Heartbeat beat) {
		if (now() - beat.publicationTime() < MAX_BEAT_AGE) return false;
		
		my(BlinkingLights.class).turnOn(LightType.WARN, "Time mismatch with " + contact(beat), "You have received an old Heartbeat from " + contact(beat) + ". This can happen if your clock and his are set to different times or to same times but different timezones.");
		return true;
	}


	private long now() {
		return my(Clock.class).time().currentValue();
	}



	private Contact contact(Heartbeat beat) {
		return my(Seals.class).contactGiven(beat.publisher());
	}


	@Override
	synchronized public void consume(Heartbeat beat) {
		if (isMyOwn(beat)) return;
		if (isTooOld(beat)) return;
		
		Contact contact = contact(beat);
		_lastBeatTimesByContact.put(contact, now());

		setAlive(contact);
	}


	private boolean isMyOwn(Heartbeat beat) {
		return my(Seals.class).ownSeal().equals(beat.publisher());
	}


	private void setAlive(Contact contact) {
		if (isAlive(contact).currentValue()) return;
		isAliveRegister(contact).setter().consume(true);
		my(Logger.class).log("Contact {} is online.", contact);
	}

	private void setDead(Contact contact) {
		if (!isAlive(contact).currentValue()) return;
		isAliveRegister(contact).setter().consume(false);
		my(Logger.class).log("Contact {} is offline.", contact);
	}




	@Override
	public boolean step() {
		notifyDeathOfStaleContacts();
		return true;
	}

}
