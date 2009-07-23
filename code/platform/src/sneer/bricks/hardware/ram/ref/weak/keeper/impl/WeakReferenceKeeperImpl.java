package sneer.bricks.hardware.ram.ref.weak.keeper.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.Map;
import java.util.WeakHashMap;

import sneer.bricks.hardware.clock.timer.Timer;
import sneer.bricks.hardware.cpu.threads.OldSteppable;
import sneer.bricks.hardware.ram.ref.weak.keeper.WeakReferenceKeeper;

public class WeakReferenceKeeperImpl implements WeakReferenceKeeper { // Refactor: in-line this brick, it's too simple 

	private final Map<Object, Object> _weakMap = new WeakHashMap<Object, Object>();

	{
		my(Timer.class).wakeUpEvery(5000, new OldSteppable() { @Override public boolean step() {
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
