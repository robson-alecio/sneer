package sneer.bricks.skin.widgets.reactive.autoscroll.impl;

import javax.swing.BoundedRangeModel;
import javax.swing.JScrollPane;

import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.CollectionSignal;
import sneer.bricks.skin.widgets.reactive.autoscroll.AutoScroll;
import sneer.foundation.lang.Consumer;

public class AutoScrollImpl implements AutoScroll {

	@Override
	public <T> JScrollPane create(CollectionSignal<T> inputSignal, Consumer<CollectionChange<T>> receiver) {
		return new OldAutoScroll<T>(inputSignal, receiver).scrollPane();
	}

	@Override
	public <T> JScrollPane create(EventSource<T> eventSource) {
		return new OldAutoScroll<T>(eventSource).scrollPane();
	}


	@Override
	public <T> JScrollPane create(EventSource<T> eventSource, Consumer<T> receiver) {
		return new OldAutoScroll<T>(eventSource, receiver).scrollPane();
	}
	
	
	@Override
	public void runWithAutoscroll(JScrollPane scrollPane, Runnable runnable) {
		boolean wasAtEnd = isAtEnd(scrollPane);
		runnable.run();
		if (wasAtEnd) placeAtEnd(scrollPane);
	}
	
	
	private boolean isAtEnd(final JScrollPane scrollPane) {
		BoundedRangeModel model = model(scrollPane);
		return model.getValue() + model.getExtent() == model.getMaximum();
	}		
	
	
	private void placeAtEnd(final JScrollPane scrollPane) {
		BoundedRangeModel model = model(scrollPane);
		model.setValue(model.getMaximum() - model.getExtent());
	}
	
	
	private BoundedRangeModel model(JScrollPane scrollPane) {
		return scrollPane.getVerticalScrollBar().getModel();
	}
}
