package sneer.bricks.hardware.ram.ref.weak.keeper;

import sneer.foundation.brickness.Brick;

@Brick
public interface WeakReferenceKeeper {

	/** Keeps a reference to the object toBeHeld and a weak reference to holder. When the holder is gc'd, the reference to the object toBeHeld is cleared, just like a WeakHashMap does. Returns holder for convenience. */
	<T> T keep(T holder, Object toBeHeld);

}
