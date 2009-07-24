package sneer.bricks.pulp.reactive.collections.impl;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.pulp.reactive.collections.ListChange;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.pulp.reactive.collections.ListChange.Visitor;
import sneer.foundation.lang.Consumer;

public abstract class VisitingListReceiver<T> implements Consumer<ListChange<T>>, Visitor<T> {

	@SuppressWarnings("unused")
	private final Contract _refToAvoidGc;

	public VisitingListReceiver(ListSignal<T> input) {
		_refToAvoidGc = input.addListReceiver(this);
	}

	@Override
	public void consume(ListChange<T> listChange) {
		listChange.accept(this);
	}
}
