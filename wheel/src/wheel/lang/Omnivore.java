package wheel.lang;

/** Can consume any value without ever throwing IllegalParameter. */
public interface Omnivore<T> extends Consumer<T> {

	void consume(T value);
	
}
