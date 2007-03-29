package wheel.reactive;


/** A List that can only be added to. Its elements cannot be removed or changed. */
public interface Journal<VO> extends ListSignal<VO> {
	public void add(VO valueObject);

}

class Todo {
	private int todo_remove_extends_create_getter_for_signal;
}
