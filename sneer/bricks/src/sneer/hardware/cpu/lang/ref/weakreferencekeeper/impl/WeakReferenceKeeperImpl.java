package sneer.hardware.cpu.lang.ref.weakreferencekeeper.impl;

import java.util.Map;
import java.util.WeakHashMap;

import sneer.hardware.cpu.lang.ref.weakreferencekeeper.WeakReferenceKeeper;

public class WeakReferenceKeeperImpl implements WeakReferenceKeeper {

	private final Map<Object, Object> _weakMap = new WeakHashMap<Object, Object>();

	@Override
	public <T> T keep(T holder, Object toBeHeld) {
		_weakMap.put(holder, toBeHeld);
		return holder;
	}

}
