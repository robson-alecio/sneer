package sneer.skin.taskpane;

import java.awt.Container;

public interface TaskPaneFactory<CONTAINER extends Container> {

	Container createContainer();

	CONTAINER createTaskPane(String title);

	CONTAINER createTaskPane(String title, boolean animated);
}