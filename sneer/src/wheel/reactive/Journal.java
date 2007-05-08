package wheel.reactive;

import wheel.reactive.list.ListSignal;


/** A List that can only be added to. Its elements cannot be removed or changed. */
public interface Journal<VO> extends ListSignal<VO> { //TODO remove extends. create getter for signal;

	public void add(VO valueObject);

}

