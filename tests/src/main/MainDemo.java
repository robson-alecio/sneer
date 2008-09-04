package main;

import javax.swing.SwingUtilities;

import sneer.kernel.container.ContainerUtils;

public class MainDemo {

	public static void main(String[] args) throws Exception {

		ContainerUtils.newContainer().produce(MainDemoBrick.class);

		waitUntilTheGuiThreadStarts();
	}

	private static void waitUntilTheGuiThreadStarts()	throws Exception {
		SwingUtilities.invokeAndWait(new Runnable(){@Override public void run() {}});
	}
}
