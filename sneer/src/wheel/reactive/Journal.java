package wheel.reactive;


/** A List that can only be added to. Its elements cannot be removed or changed. */
public interface Journal extends ListSignal {

	public void add(Object object);

}
