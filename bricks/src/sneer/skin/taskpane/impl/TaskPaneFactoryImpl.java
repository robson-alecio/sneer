package sneer.skin.taskpane.impl;

import java.awt.Container;
import java.awt.Graphics2D;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.painter.Painter;

import sneer.skin.taskpane.TaskPaneFactory;

public class TaskPaneFactoryImpl implements TaskPaneFactory<JXTaskPane> {

	@SuppressWarnings("unchecked")
	@Override
	public Container createContainer() {
		JXTaskPaneContainer container = new JXTaskPaneContainer();
	    container.setBackgroundPainter(new Painter(){
			@Override
			public void paint(Graphics2D g, Object object, int width, int height) {
				//ignore painter
			}}
	    );
	    container.setOpaque(false);
	    container.setBorder(null);
	    container.setAlpha(0.9f);
		return container;
	}

	@Override
	public JXTaskPane createTaskPane(String title) {
		return createTaskPane(title, true);
	}

	@Override
	public JXTaskPane createTaskPane(String title,	boolean animated) {
		JXTaskPane tp = new JXTaskPane();
	    tp.setAnimated(animated);
	    tp.setTitle(title);
		return tp;	
	}

}
