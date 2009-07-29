package sneer.bricks.skin.widgets.autoscroll.impl;

import static sneer.foundation.environments.Environments.my;

import javax.swing.BoundedRangeModel;
import javax.swing.JScrollPane;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.skin.widgets.autoscroll.AutoScroll;

public class AutoScrollImpl implements AutoScroll {

	@Override
	public void runWithAutoscroll(final JScrollPane scrollPane, Runnable runnable) {
		final boolean wasAtEnd = isAtEnd(scrollPane);
//		System.out.println(wasAtEnd);
		
		runnable.run();
		
		my(GuiThread.class).invokeLater(new Runnable() { @Override public void run() {
			if (wasAtEnd)
				placeAtEnd(scrollPane);
			
//			System.out.println(model(scrollPane).getValue());
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
