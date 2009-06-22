package sneer.bricks.hardware.ram.ref.immutable;

public interface Immutable<T> {

	void set(T value);
	T get();
	
}
