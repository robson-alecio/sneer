package wheelexperiments.reactive;

public interface Source<T> extends Signal<T> {

	public void supply(T value);

}
