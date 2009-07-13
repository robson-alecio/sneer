package sneer.bricks.skin.widgets.reactive.autoscroll.impl;

import javax.swing.BoundedRangeModel;
import javax.swing.JScrollPane;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.skin.widgets.reactive.autoscroll.AutoScroll;
import sneer.foundation.lang.Consumer;
import static sneer.foundation.environments.Environments.my;

public class AutoScrollImpl implements AutoScroll {

	@Override
	public <T> JScrollPane create(EventSource<T> eventSource, Consumer<T> receiver) {
		return new OldAutoScroll<T>(eventSource, receiver).scrollPane();
	}
	
	
	@Override
	public void runWithAutoscroll(final JScrollPane scrollPane, Runnable runnable) {
		final int position = model(scrollPane).getValue();
		final boolean wasAtEnd = isAtEnd(scrollPane);
		
		runnable.run();
		
		my(GuiThread.class).invokeLater(new Runnable() { @Override public void run() {
			if (wasAtEnd)
				placeAtEnd(scrollPane);
			else
				model(scrollPane).setValue(position);
		}});
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
