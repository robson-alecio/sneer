package sneer.skin.mainframe.tests;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.skin.mainframe.MainFrame;
import wheel.lang.Threads;

public class MainFrameDemo  {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();
		container.produce(MainFrame.class);
		Threads.sleepWithoutInterruptions(30000);
	}
}
