package sneer.hardware.cpu.lang.ref.weakreferencekeeper.impl;

import static sneer.commons.environments.Environments.my;

import java.util.Map;
import java.util.WeakHashMap;

import sneer.hardware.cpu.lang.ref.weakreferencekeeper.WeakReferenceKeeper;
import sneer.pulp.clock.Clock;
import sneer.pulp.threads.Stepper;

public class WeakReferenceKeeperImpl implements WeakReferenceKeeper { // Refactor: in-line this brick, it's too simple 

	private final Map<Object, Object> _weakMap = new WeakHashMap<Object, Object>();

	{
		my(Clock.class).wakeUpEvery(5000, new Stepper() { @Override public boolean step() {
			forceWeakMapToCleanStaleReferences();
			return true;
		}});
	}

	@Override
	public <T> T keep(T holder, Object toBeHeld) {
		_weakMap.put(holder, toBeHeld);

		return holder;
	}

	private void forceWeakMapToCleanStaleReferences() {
		_weakMap.size();
	}
}
