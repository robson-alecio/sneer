package sneer.foundation.lang;

/** Can consume any value without ever throwing Refusal. */
public interface Consumer<T> extends PickyConsumer<T> {

	void consume(T value);
	
}
