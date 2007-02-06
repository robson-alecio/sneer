package wheel.reactive;

public interface Source<T> extends Signal<T> {

	public void supply(T value);
	
	public boolean isSameValue(T value);

}
