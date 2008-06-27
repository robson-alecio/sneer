package sneer.skin.taskpane.impl;

import java.awt.Container;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import sneer.skin.taskpane.TaskPaneFactory;

public class TaskPaneFactoryImpl implements TaskPaneFactory<JXTaskPane> {

	@SuppressWarnings("unchecked")
	@Override
	public Container createContainer() {
		JXTaskPaneContainer container = new JXTaskPaneContainer();
		container.setOpaque(false);
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