package wheel.lang;

/** Can consume any value without ever throwing IllegalParameter. */
public interface Consumer<T> extends PickyConsumer<T> {

	void consume(T value);
	
}
